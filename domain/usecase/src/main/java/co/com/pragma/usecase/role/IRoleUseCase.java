package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import reactor.core.publisher.Mono;

public interface IRoleUseCase {
    Mono<Role> findRoleById(Long idRole);
}
