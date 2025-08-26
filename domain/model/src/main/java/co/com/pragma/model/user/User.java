package co.com.pragma.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String name;
    private String lastName;
    private Date birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal baseSalary;
}
