package co.com.crediya.api.validator;

import co.com.crediya.api.exceptions.ValidationException;
import co.com.crediya.model.responsecode.ResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class GenericValidator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Mono<T> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        return violations.isEmpty()
                ? Mono.just(object)
                : Mono.error(new ValidationException(formatErrors(violations), ResponseCode.DATA_CORRUPTED, ResponseCode.MESSAGE_DATA_CORRUPTED));
    }

    private static <T> Map<String, List<String>> formatErrors(Set<ConstraintViolation<T>> violations) {
        List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return Map.of("errors", errors);
    }


}
