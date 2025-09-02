package co.com.crediya.api.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRequest {

    @NotBlank(message = "names is required")
    private String names;

    @NotBlank(message = "last names is required")
    private String lastNames;

    @NotBlank(message = "date of birth is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "date of birth must follow the format yyyy-MM-dd"
    )
    private String birthDate;

    @NotBlank(message = "document number is required")
    private String documentNumber;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    private String email;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "idRole is required")
    private Long idRole;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotNull(message = "base salary is required")
    @DecimalMin(value = "0.0", message = "base salary must be >= 0")
    @DecimalMax(value = "15000000.0", message = "base salary must be <= 15000000")
    private BigDecimal salaryBase;

}
