package co.com.crediya.model.responsecode;

public class ResponseCode {

    private ResponseCode() { throw new IllegalStateException("Utility class");}

    public static final String USER_CREATED_SUCCESSFULLY = "AUTH-SUCCESS001";
    public static final String USER_FILTERED_SUCCESSFULLY = "AUTH-SUCCESS002";

    public static final String DUPLICATE_EMAIL = "AUTH-ERROR001";
    public static final String DUPLICATE_IDENTIFICATION = "AUTH-ERROR002";
    public static final String USER_NOT_EXISTS = "AUTH-ERROR003";
    public static final String TECHNICAL_ERROR = "AUTH-ERROR004";
    public static final String DATA_CORRUPTED = "AUTH-ERROR005";
    public static final String DATA_BASE_FAILED = "AUTH-ERROR006";
}
