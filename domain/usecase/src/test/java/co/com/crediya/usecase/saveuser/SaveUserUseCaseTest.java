package co.com.crediya.usecase.saveuser;

import co.com.crediya.model.exceptions.BusinessException;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SaveUserUseCase saveUserUseCase;

    private User newUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        newUser = User.builder()
                .id(null)
                .names("Jane")
                .lastNames("Smith")
                .email("jane.smith@example.com")
                .salaryBase(BigDecimal.valueOf(2000000))
                .build();

        existingUser = User.builder()
                .id(1L)
                .names("Jane")
                .lastNames("Smith")
                .email("jane.smith@example.com")
                .salaryBase(BigDecimal.valueOf(2000000))
                .build();
    }

    @Test
    void shouldSaveNewUserWhenNotExists() {
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.save(newUser)).thenReturn(Mono.just(newUser));
        Mono<User> result = saveUserUseCase.execute(newUser);
        StepVerifier.create(result)
                .expectNext(newUser)
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserAlreadyExists() {
        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Mono.just(existingUser));
        Mono<User> result = saveUserUseCase.execute(existingUser);
        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();

    }

}
