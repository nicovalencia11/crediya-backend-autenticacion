package co.com.crediya.model.exceptions;

import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final String code;
    private final String message;

    public TechnicalException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
