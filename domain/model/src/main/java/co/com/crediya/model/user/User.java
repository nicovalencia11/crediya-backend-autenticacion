package co.com.crediya.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;
    private String names;
    private String lastNames;
    private LocalDate birthDate;
    private String documentNumber;
    private String email;
    private String address;
    private String phone;
    private String password;
    private Long idRole;
    private BigDecimal salaryBase;

}
