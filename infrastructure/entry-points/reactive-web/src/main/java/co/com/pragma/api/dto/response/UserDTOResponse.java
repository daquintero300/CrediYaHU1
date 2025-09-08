package co.com.pragma.api.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTOResponse {
    private String name;
    private String lastName;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal baseSalary;
    private Long idNumber;
    private Long idRole;
    private String password;
}
