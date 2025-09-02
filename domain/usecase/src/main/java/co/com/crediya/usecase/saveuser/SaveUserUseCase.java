package co.com.crediya.usecase.saveuser;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.responsecode.ResponseCode;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SaveUserUseCase {

    private final UserRepository userRepository;

    public Mono<User> execute(User user) {
        return userRepository.findByEmail(user.getEmail())
                .hasElement()
                .flatMap(emailExists -> emailExists
                        ? Mono.error(new BusinessException(ResponseCode.DUPLICATE_EMAIL))
                        : Mono.just(user))
                .flatMap(userValidateWithoutEmail -> userRepository.findByDocumentNumber(userValidateWithoutEmail.getDocumentNumber())
                        .hasElement()
                        .flatMap(idExists -> idExists
                                ? Mono.error(new BusinessException(ResponseCode.DUPLICATE_IDENTIFICATION))
                                : userRepository.save(userValidateWithoutEmail)
                        )
                );
    }

}
