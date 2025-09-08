package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginDTORequest(@NotBlank
                               String username,

                                  @NotBlank
                               String password) {
}
