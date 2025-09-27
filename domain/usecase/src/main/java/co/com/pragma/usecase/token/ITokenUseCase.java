package co.com.pragma.usecase.token;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ITokenUseCase {
    Mono<Map<String, String>> createToken(User user, Role role);
}
