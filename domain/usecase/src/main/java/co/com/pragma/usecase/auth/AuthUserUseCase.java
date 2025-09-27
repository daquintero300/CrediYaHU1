package co.com.pragma.usecase.auth;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import co.com.pragma.usecase.token.ITokenUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequiredArgsConstructor
public class AuthUserUseCase implements IAuthUserUseCase{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ITokenUseCase iTokenUseCase;
    private final IPasswordEncoderUseCase iPasswordEncoderUseCase;

    @Override
    public Mono<Map<String, String>> login(String email, String password) {
        if (email == null || email.isBlank()) {
            return Mono.error(new IllegalArgumentException("EL email no puede ser nulo o vació"));
        }
        if (password == null || password.isBlank()) {
            return Mono.error(new IllegalArgumentException("EL password no puede ser nulo o vació"));
        }
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El usuario no existe")))
                .flatMap(user ->
                    iPasswordEncoderUseCase.matches(password, user.getPassword())
                            .flatMap( matches -> {
                                if (!matches) {
                                    return Mono.error(new IllegalArgumentException("Contraseña incorrecta"));
                                }
                                return  roleRepository.findRoleById(user.getIdRole())
                                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Rol no encontrado")))
                                        .flatMap(role -> iTokenUseCase.createToken(user, role));
                            })
                )
                .flatMap(token -> Mono.just(token));
    }
}
