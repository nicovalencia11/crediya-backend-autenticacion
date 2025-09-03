package co.com.crediya.usecase.filteruserbyidentification;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterUserByIdentificationUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FilterUserByIdentificationUseCase filterUserByIdentificationUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .names("Jane")
                .lastNames("Smith")
                .email("jane.smith@example.com")
                .documentNumber("12345678")
                .birthDate(LocalDate.of(1998, 9, 8))
                .salaryBase(BigDecimal.valueOf(2000000))
                .build();
    }

    @Test
    void shouldReturnUserWhenExists() {
        when(userRepository.findByDocumentNumber("12345678")).thenReturn(Mono.just(user));
        Mono<User> result = filterUserByIdentificationUseCase.execute("12345678");
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenUserNotExists() {
        when(userRepository.findByDocumentNumber("99999999")).thenReturn(Mono.empty());
        Mono<User> result = filterUserByIdentificationUseCase.execute("99999999");
        StepVerifier.create(result)
                .verifyComplete();
    }
}
