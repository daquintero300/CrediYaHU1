package co.com.pragma.usecase.auth;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import co.com.pragma.usecase.token.ITokenUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class AuthUserUseCase implements IAuthUserUseCase{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ITokenUseCase iTokenUseCase;
    private final IPasswordEncoderUseCase iPasswordEncoderUseCase;

    @Override
    public Mono<String> login(String email, String password) {

        if (email == null || email.isBlank()) {
            return Mono.error(new IllegalArgumentException("The email cannot be null or empty"));
        }
        if (password == null || password.isBlank()) {
            return Mono.error(new IllegalArgumentException("The password cannot be null or empty"));
        }

        System.out.println("name " + email
                + "\npassword " + password);

        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    System.out.println(user.getName());
                    return Mono.just(user);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User does not exist")))
                .flatMap(user ->
                    iPasswordEncoderUseCase.matches(password, user.getPassword())
                            .flatMap( matches -> {
                                if (!matches) {
                                    return Mono.error(new IllegalArgumentException("Contraseña incorrecta"));
                                }
                                return  roleRepository.findRoleById(user.getIdRole())
                                        .switchIfEmpty(Mono.error(new IllegalArgumentException("Role not found")))
                                        .flatMap(role -> iTokenUseCase.createToken(user, role));
                            })
                )
                .flatMap(token -> {
                    System.out.println(token);
                    return Mono.just(token);
                });
    }
}
