package co.com.pragma.api;

import co.com.pragma.api.dto.request.UserDTORequest;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.usecase.user.IUserUseCase;
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
public class UserHandler {

    private final Validator validator;
    private final IUserUseCase iUserUseCase;

    public Mono<ServerResponse> listenSaveUser(@Validated ServerRequest serverRequest) {
        log.info("[INFO] Ejecutando saveUser() de UserHandler");
        return serverRequest.bodyToMono(UserDTORequest.class)
                .doOnNext(body -> log.info("📥 Payload recibido para guardar usuario: {}", body))
                .flatMap(this::validate)
                .flatMap(UserMapper::toUserDomain)
                .flatMap(iUserUseCase::saveUser)
                .flatMap(UserMapper::toUserDTOResponse)
                .doOnSuccess(user -> log.info("[INFO] Usuario guardado exitosamente"))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .doOnError(error -> log.error("[ERROR] Error al guardar usuario", error));
    }

    public Mono<ServerResponse> listenFindAllUsers(ServerRequest serverRequest) {
        log.info("[INFO] Ejecutando listenFindAllUsers() de UserHandler");
        return iUserUseCase.findAllUsers()
                .flatMap(UserMapper::toUserDTOResponse)
                .collectList()
                .doOnSuccess(users ->
                        log.info("[INFO] Usuarios obtenidos: {}", users.size()))
                .flatMap(users -> {
                    System.out.println(users);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(users);
                })
                .doOnError(error ->
                        log.error("[ERROR] Error al obtener usuarios", error));
    }

    public Mono<ServerResponse> listenFindUserByEmail(ServerRequest serverRequest) {
        log.info("[INFO] Ejecutando listenFindUserByEmail() de UserHandler");
        return Mono.justOrEmpty(serverRequest.queryParam("email"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El email es obligatorio")))
                .flatMap(iUserUseCase::findByEmail)
                .flatMap(UserMapper::toUserDTOResponse)
                .doOnSuccess(user ->
                        log.info("[INFO] Usuario obtenido correctamente"))
                .flatMap(user -> {
                    System.out.println(user);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user);
                })
                .doOnError(error ->
                        log.error("[ERROR] Error al obtener usuario por email", error));
    }

    private <T> Mono<T> validate(T dto) {
        var errors = validator.validate(dto);
        if (!errors.isEmpty()) {
            return Mono.error(new ConstraintViolationException(errors));
        }
        return Mono.just(dto);
    }
}
