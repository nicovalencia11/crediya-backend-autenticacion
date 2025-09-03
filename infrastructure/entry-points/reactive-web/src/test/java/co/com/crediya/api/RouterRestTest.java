package co.com.crediya.api;

import co.com.crediya.api.exceptions.GlobalExceptionHandler;
import co.com.crediya.api.handler.UserHandler;
import co.com.crediya.api.request.UserRequest;
import co.com.crediya.api.response.ApiResponse;
import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.filteruserbyidentification.FilterUserByIdentificationUseCase;
import co.com.crediya.usecase.saveuser.SaveUserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class, GlobalExceptionHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SaveUserUseCase saveUserUseCase;

    @MockitoBean
    private FilterUserByIdentificationUseCase filterUserByIdentificationUseCase;


    private UserRequest buildValidUserRequest() {
        return UserRequest.builder()
                .names("Nicolas")
                .lastNames("Valencia")
                .birthDate("1998-09-08")
                .documentNumber("1094970200")
                .address("Calle 123")
                .phone("3217802697")
                .email("nicolas@gmai.com")
                .salaryBase(BigDecimal.valueOf(1000000))
                .build();
    }

    private User buildDomainUser() {
        return User.builder()
                .id(1L)
                .names("Nicolas")
                .lastNames("Valencia")
                .birthDate(LocalDate.of(1998, 9, 8))
                .documentNumber("1094970200")
                .address("Calle 123")
                .phone("3217802697")
                .email("nicolas@gmai.com")
                .salaryBase(BigDecimal.valueOf(1000000))
                .build();
    }

    @Test
    void testPOSTCreateUser_CREATED() {
        given(saveUserUseCase.execute(any()))
                .willReturn(Mono.just(buildDomainUser()));

        webTestClient.post()
                .uri("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildValidUserRequest())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ApiResponse.class)
                .value(apiResponse -> {
                    Assertions.assertThat(apiResponse.getData()).isNotNull();
                });
    }

    @Test
    void testGETUserByIdentification_OK() {
        given(filterUserByIdentificationUseCase.execute("1094970200"))
                .willReturn(Mono.just(buildDomainUser()));
        webTestClient.get()
                .uri("/v1/user/{identification}", "1094970200")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<User>>() {})
                .value(apiResponse -> {
                    Assertions.assertThat(apiResponse.getData()).isNotNull();
                    Assertions.assertThat(apiResponse.getData().getDocumentNumber()).isEqualTo("1094970200");
                    Assertions.assertThat(apiResponse.getData().getNames()).isEqualTo("Nicolas");
                });
    }

    @Test
    void testPOSTCreateUser_ValidationError() {
        UserRequest invalidRequest = UserRequest.builder()
                .lastNames("Valencia")
                .birthDate("1998-09-08")
                .documentNumber("1094970200")
                .address("Calle 123")
                .phone("3217802697")
                .email("nicolas@gmai.com")
                .salaryBase(BigDecimal.valueOf(1000000))
                .build();
        webTestClient.post()
                .uri("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPOSTCreateUser_ErrorInUseCase() {
        given(saveUserUseCase.execute(any()))
                .willReturn(Mono.error(new BusinessException("Error mock")));
        webTestClient.post()
                .uri("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(buildValidUserRequest())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiResponse.class)
                .value(apiResponse -> {
                    Assertions.assertThat(apiResponse.getCode()).isEqualTo("Error mock");
                });
    }
}