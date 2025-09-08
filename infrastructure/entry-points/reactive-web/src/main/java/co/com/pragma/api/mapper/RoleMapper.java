package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.RoleDTO;
import co.com.pragma.model.role.Role;
import reactor.core.publisher.Mono;

public class RoleMapper {
    public static Mono<Role> toRoleDomain(RoleDTO roleDTO) {
        return Mono.just(Role.builder()
                        .roleName(roleDTO.getName())
                        .roleDescription(roleDTO.getDescription())
                .build());
    }
}
