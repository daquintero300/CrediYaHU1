package co.com.pragma.usecase.auth;

import reactor.core.publisher.Mono;

public interface IAuthUserUseCase {
    Mono<String> login(String email, String password);
}
