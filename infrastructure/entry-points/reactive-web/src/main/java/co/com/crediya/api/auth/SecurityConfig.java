package co.com.crediya.api.auth;

import co.com.crediya.api.auth.filter.JwtTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtTokenValidatorFilter jwtTokenValidatorFilter;

    public SecurityConfig(JwtTokenValidatorFilter jwtTokenValidatorFilter) {
        this.jwtTokenValidatorFilter = jwtTokenValidatorFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .pathMatchers("/doc/swagger-ui/**", "/doc/api-docs/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/events/**").hasAuthority("READ")
                        .pathMatchers(HttpMethod.PUT, "/events/**").hasAuthority("WRITE")
                        .pathMatchers(HttpMethod.DELETE, "/events/**").hasAuthority("WRITE")
                        .pathMatchers(HttpMethod.POST, "/events/**").hasAuthority("WRITE")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtTokenValidatorFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
