package co.com.pragma.usecase.auth;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface IAuthUserUseCase {
    Mono<Map<String, String>> login(String email, String password);
}
