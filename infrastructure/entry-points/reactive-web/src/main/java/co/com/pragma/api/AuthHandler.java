package co.com.pragma.api;

import co.com.pragma.api.dto.request.AuthLoginDTORequest;
import co.com.pragma.usecase.auth.IAuthUserUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final IAuthUserUseCase iAuthUserUseCase;
    private final Validator validator;

    public Mono<ServerResponse> listenLoginUser(@Validated ServerRequest serverRequest) {
        log.info("➡️ Se recibio la peticion de login");
        return serverRequest.bodyToMono(AuthLoginDTORequest.class)
                .flatMap(this::validate)
                .flatMap(authLoginRequest -> {
                    System.out.println("email " + authLoginRequest.username()
                            + "\npassword " + authLoginRequest.password());
                    return iAuthUserUseCase.login(authLoginRequest.username(), authLoginRequest.password());
                })
                .flatMap(token -> {
                    log.info("✅ Login exitoso");
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(token);
                })
                .onErrorResume(e -> {
                    log.error("❌ Error durante el login: {}", e.getMessage(), e);
                    return ServerResponse.status(401)
                            .bodyValue("Credenciales inválidas");
                });
    }

    private <T> Mono<T> validate(T dto) {
        var errors = validator.validate(dto);
        if (!errors.isEmpty()) {
            return Mono.error(new ConstraintViolationException(errors));
        }
        return Mono.just(dto);
    }
}
