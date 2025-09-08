package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase implements IRoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> findRoleById(Long idRole) {
        return roleRepository.findRoleById(idRole);
    }
}
