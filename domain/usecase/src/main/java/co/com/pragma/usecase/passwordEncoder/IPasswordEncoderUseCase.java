package co.com.pragma.usecase.passwordEncoder;

import reactor.core.publisher.Mono;

public interface IPasswordEncoderUseCase {
    Mono<String> encode(String password);
    Mono<Boolean> matches(String password, String encodePassword);
}
