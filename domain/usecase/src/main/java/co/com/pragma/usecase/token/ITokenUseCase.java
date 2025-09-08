package co.com.pragma.usecase.token;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

public interface ITokenUseCase {
    Mono<String> createToken(User user, Role role);
}
