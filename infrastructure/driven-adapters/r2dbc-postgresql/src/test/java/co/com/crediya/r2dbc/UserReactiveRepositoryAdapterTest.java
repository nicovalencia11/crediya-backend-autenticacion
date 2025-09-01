package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    private UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private final UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .names("John")
            .lastNames("Doe")
            .email("john.doe@test.com")
            .documentNumber("123456789")
            .phone("555-1234")
            .idRole(2L)
            .salaryBase(BigDecimal.valueOf(1000000))
            .build();

    private final User user = new User(
            1L,
            "John",
            "Doe",
            LocalDate.parse("1990-01-01"),
            "123456789",
            "john.doe@test.com",
            "address 123",
            "321654987",
            null,
            BigDecimal.valueOf(1000000));

    @Test
    void mustSaveValue() {
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }
}
