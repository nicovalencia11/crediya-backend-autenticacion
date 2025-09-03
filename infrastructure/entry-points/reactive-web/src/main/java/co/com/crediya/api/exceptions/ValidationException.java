package co.com.crediya.api.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException{

    private final String code;
    private final Map<String, List<String>> data;

    public ValidationException(Map<String, List<String>> stringListMap, String code, String message) {
        super(message);
        this.data = stringListMap;
        this.code = code;
    }

}
