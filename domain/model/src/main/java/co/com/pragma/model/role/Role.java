package co.com.pragma.model.role;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Role {
    private String roleName;
    private String roleDescription;
}
