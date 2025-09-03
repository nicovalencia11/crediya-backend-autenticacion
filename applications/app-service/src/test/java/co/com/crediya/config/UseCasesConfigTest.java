package co.com.crediya.config;

import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.filteruserbyidentification.FilterUserByIdentificationUseCase;
import co.com.crediya.usecase.saveuser.SaveUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {
            assertNotNull(context.getBean(FilterUserByIdentificationUseCase.class));
            assertNotNull(context.getBean(SaveUserUseCase.class));
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }
}
