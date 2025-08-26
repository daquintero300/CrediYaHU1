package co.com.pragma.api.dto;

import co.com.pragma.model.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    @NotBlank
    private String name;
    private String lastName;
    private Date birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal baseSalary;

    public User toUser() {
        return new User(name, lastName, birthdate, address, phoneNumber, email, baseSalary);
    }
}
