package co.com.pragma.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "the user entity info")
public class UserDTORequest{

    @NotNull()
    @NotBlank
    @Schema(description = "user name", example = "David")
    private String name;

    @NotNull
    @NotBlank
    @Schema(description = "last name", example = "Quintero")
    private String lastName;

    @Schema(description = "birthdate", example = "22/12/2025")
    private LocalDate birthdate;

    @NotBlank
    @Schema(description = "user address", example = "Calle 123")
    private String address;

    @Pattern(
            regexp = "^\\+?\\d{1,3}?[- .]?\\d{7,12}$",
            message = "El número de teléfono no es válido"
    )
    @Schema(description = "Phone number", example = "+573001234567")
    private String phoneNumber;

    @NotNull
    @NotBlank
    @Email(message = "El formato del correo no es válido")
    @Schema(description = "email address", example = "davidquintero@gmail.com")
    private String email;

    @NotNull
    @DecimalMin(value = "0.0",
            inclusive = true,
            message = "El valor no puede ser menor que 0")
    @DecimalMax(value = "15000000.0",
            inclusive = true,
            message = "El valor no puede superar 15,000,000")
    @Schema(description = "base salary", example = "3500000.00")
    private BigDecimal baseSalary;

    @NotNull
    @Schema(description = "id number", example = "123456789")
    private Long idNumber;

    @NotNull
    @Schema(description = "password", example = "123456qg")
    private String password;

    @NotNull
    @Schema(description = "role id", example = "1")
    private Long idRole;
}
