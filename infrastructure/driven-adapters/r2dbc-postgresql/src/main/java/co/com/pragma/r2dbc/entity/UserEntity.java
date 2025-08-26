package co.com.pragma.r2dbc.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("user_id")
    private String id;

    @NotNull
    @NotBlank
    @Column("name")
    private String name;

    @NotNull
    @NotBlank
    @Column("last_name")
    private String lastName;

    @Column("birthdate")
    private Date birthdate;

    @Column("address")
    private String address;

    @Pattern(
            regexp = "^\\+?\\d{1,3}?[- .]?\\d{7,12}$",
            message = "El número de teléfono no es válido"
    )
    @Column("phone_number")
    private String phoneNumber;

    @NotNull
    @NotBlank
    @Email(message = "El formato del correo no es válido")
    @Column("email")
    private String email;

    @NotNull
    @NotBlank
    @DecimalMin(value = "0.0",
            inclusive = true,
            message = "El valor no puede ser menor que 0")
    @DecimalMax(value = "15000000.0",
            inclusive = true,
            message = "El valor no puede superar 15,000,000")
    @Column("base_salary")
    private BigDecimal baseSalary;
}
