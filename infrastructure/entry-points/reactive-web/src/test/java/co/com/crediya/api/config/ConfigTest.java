package co.com.crediya.api.config;

import co.com.crediya.api.RouterRest;
import co.com.crediya.api.handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({RouterRest.class, CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @SpringBootConfiguration
    static class TestBootConfig {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserHandler userHandler;

    @BeforeEach
    void setUpRoutes() {
        when(userHandler.listenGETFilteredUserByIdentificationUseCase(any(ServerRequest.class)))
                .thenAnswer(inv -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue("ok"));
        when(userHandler.listenPOSTCreateUserUseCase(any(ServerRequest.class)))
                .thenAnswer(inv -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"status\":\"ok\"}"));
    }

    @Test
    void corsAndSecurityHeaders_areApplied() {
        webTestClient.get()
                .uri("/v1/user/123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }
}