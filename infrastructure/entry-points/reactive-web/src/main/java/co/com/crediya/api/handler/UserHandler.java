package co.com.crediya.api.handler;

import co.com.crediya.api.mapper.UserRequestMapper;
import co.com.crediya.api.request.UserRequest;
import co.com.crediya.api.response.ApiResponse;
import co.com.crediya.api.validator.GenericValidator;
import co.com.crediya.model.responsecode.ResponseCode;
import co.com.crediya.usecase.filteruserbyidentification.FilterUserByIdentificationUseCase;
import co.com.crediya.usecase.saveuser.SaveUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final SaveUserUseCase saveUserUseCase;
    private final FilterUserByIdentificationUseCase filterUserByIdentificationUseCase;

    public Mono<ServerResponse> listenPOSTCreateUserUseCase(ServerRequest serverRequest) {
        log.info("MESSAGE_HANDLER_LOG_TRACE : INIT METHOD USER CREATE");
        return serverRequest.bodyToMono(UserRequest.class)
                .flatMap(GenericValidator::validate)
                .map(UserRequestMapper::toDomain)
                .flatMap(user -> {
                    log.info("MESSAGE_HANDLER_LOG_TRACE : Received request to create user with email={}", user.getEmail());
                    return saveUserUseCase.execute(user)
                            .doOnSuccess(u -> log.info("MESSAGE_HANDLER_LOG_TRACE : Successfully created user with id={}", u.getId()))
                            .doOnError(err -> log.error("MESSAGE_HANDLER_LOG_TRACE : Error while creating user with email={} - {}", user.getEmail(), err.getMessage()))
                            .flatMap(savedUser -> buildResponse(savedUser, HttpStatus.CREATED.value(), ResponseCode.USER_CREATED_SUCCESSFULLY, ResponseCode.MESSAGE_SUCCESSFULLY_CREATED));
                });
    }

    public Mono<ServerResponse> listenGETFilteredUserByIdentificationUseCase(ServerRequest serverRequest){
        String identification = serverRequest.pathVariable("identification");
        log.info("MESSAGE_HANDLER_LOG_TRACE : INIT METHOD FILTER USER BY IDENTIFICATION");
        return filterUserByIdentificationUseCase.execute(identification)
                .doOnSuccess(user -> log.info("MESSAGE_HANDLER_LOG_TRACE : User found with identification={}", identification))
                .doOnError(err -> log.error("MESSAGE_HANDLER_LOG_TRACE : Error searching user with identification={} - {}", identification, err.getMessage()))
                .flatMap(user -> buildResponse(user, HttpStatus.OK.value(), ResponseCode.USER_FILTERED_SUCCESSFULLY, ResponseCode.MESSAGE_SUCCESSFULLY_FILTERED))
                .switchIfEmpty(buildResponse(null, HttpStatus.NOT_FOUND.value(), ResponseCode.USER_NOT_EXISTS, ResponseCode.MESSAGE_USER_NOT_EXISTS));
    }

    private <T> Mono<ServerResponse> buildResponse(T data, int status, String code, String message) {
        return ServerResponse.status(status).bodyValue(
                ApiResponse.<T>builder()
                        .data(data)
                        .code(code)
                        .message(message)
                        .build()
        );
    }
}
