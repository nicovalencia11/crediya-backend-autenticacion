package co.com.crediya.usecase.filteruserbyidentification;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FilterUserByIdentificationUseCase {

    private final UserRepository userRepository;

    public Mono<User> execute(String identification){
        return userRepository.findByDocumentNumber(identification);
    }
}
