package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "usuario")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column("id_usuario")
    private Long id;

    @Column("nombres")
    private String names;

    @Column("apellidos")
    private String lastNames;

    @Column("fecha_nacimiento")
    private LocalDate birthDate;

    @Column("documento_identidad")
    private String documentNumber;

    @Column("correo_electronico")
    private String email;

    @Column("direccion")
    private String address;

    @Column("telefono")
    private String phone;

    @Column("contrasena")
    private String password;

    @Column("id_rol")
    private Long idRole;

    @Column("salario_base")
    private BigDecimal salaryBase;

}