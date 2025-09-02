package co.com.crediya.api.auth.filter;

import co.com.crediya.api.auth.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtTokenValidatorFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    public JwtTokenValidatorFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Valida y decodifica el JWT
                DecodedJWT decodedJWT = jwtUtils.validateToken(token);

                // Extrae el nombre de usuario y los roles (authorities)
                String username = jwtUtils.extractUsername(decodedJWT);
                String authoritiesString = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
                var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);
                System.out.println("valida token");
                // Crea un token de autenticación con el contexto de seguridad reactivo
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContext context = new SecurityContextImpl(authToken);

                // Establece el contexto de seguridad para la solicitud actual
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            } catch (Exception e) {
                // En caso de token inválido, puedes decidir si quieres continuar sin autenticación
                // o detener la cadena de filtros
                return chain.filter(exchange);
            }
        }

        // Si no hay token, sigue con la cadena de filtros sin autenticación
        return chain.filter(exchange);
    }
}
