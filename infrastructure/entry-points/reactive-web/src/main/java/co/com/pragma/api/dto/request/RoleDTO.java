package co.com.pragma.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "the user entity info")
public class RoleDTO {

    private String name;

    private String description;
}
