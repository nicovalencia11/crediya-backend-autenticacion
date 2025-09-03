package co.com.crediya.r2dbc;

import co.com.crediya.model.exceptions.TechnicalException;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    private UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator transactionalOperator;

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
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        StepVerifier.create(repositoryAdapter.save(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustMapSaveErrorToTechnicalException() {
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.error(new RuntimeException("db error")));
        StepVerifier.create(repositoryAdapter.save(user))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findByEmail_found_returnsDomain() {
        when(repository.findByEmail(eq("john.doe@test.com"))).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        StepVerifier.create(repositoryAdapter.findByEmail("john.doe@test.com"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void findByEmail_empty_completesWithoutValue() {
        when(repository.findByEmail(eq("missing@test.com"))).thenReturn(Mono.empty());
        StepVerifier.create(repositoryAdapter.findByEmail("missing@test.com"))
                .verifyComplete();
    }

    @Test
    void findByEmail_error_mapsToTechnicalException() {
        when(repository.findByEmail(eq("boom@test.com")))
                .thenReturn(Mono.error(new RuntimeException("db boom")));
        StepVerifier.create(repositoryAdapter.findByEmail("boom@test.com"))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void findByDocumentNumber_found_returnsDomain() {
        when(repository.findByDocumentNumber(eq("123456789"))).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        StepVerifier.create(repositoryAdapter.findByDocumentNumber("123456789"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void findByDocumentNumber_empty_completesWithoutValue() {
        when(repository.findByDocumentNumber(eq("000"))).thenReturn(Mono.empty());
        StepVerifier.create(repositoryAdapter.findByDocumentNumber("000"))
                .verifyComplete();
    }

    @Test
    void findByDocumentNumber_error_mapsToTechnicalException() {
        when(repository.findByDocumentNumber(eq("bad")))
                .thenReturn(Mono.error(new RuntimeException("db fail")));
        StepVerifier.create(repositoryAdapter.findByDocumentNumber("bad"))
                .expectError(TechnicalException.class)
                .verify();
    }
}
