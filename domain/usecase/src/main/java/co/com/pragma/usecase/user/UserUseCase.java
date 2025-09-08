package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final IPasswordEncoderUseCase iPasswordEncoderUseCase;

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<User> saveUser(User user) {
        return validateUser(user)
                .flatMap( userToSave -> userRepository.saveUser(user));
    }

    public Mono<User> validateUser(User user) {
        return Mono.just(user)
                .flatMap(userValidation -> {
                    if (userValidation.getName() == null || userValidation.getName().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El 'name' no puede estar vacio"));
                    }
                    if (userValidation.getLastName() == null || userValidation.getLastName().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El 'lastName' no puede estar vacio"));
                    }
                    if (userValidation.getEmail() == null || userValidation.getEmail().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El 'email' no puede estar vacio"));
                    }
                    if (userValidation.getBaseSalary() == null) {
                        return Mono.error(new IllegalArgumentException("El 'baseSalary' no puede estar vacio"));
                    }
                    if (userValidation.getBaseSalary().compareTo(BigDecimal.ZERO) < 0
                            || userValidation.getBaseSalary().compareTo(new BigDecimal("15000000")) > 0) {
                        return Mono.error(new IllegalArgumentException("El salario debe estar entre 0 y 15.000.000"));
                    }
                    if (!userValidation.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
                        return Mono.error(new IllegalArgumentException("El formato del correo no es válido"));
                    }
                    if (userValidation.getPassword() == null || userValidation.getPassword().isBlank()) {
                        return Mono.error(new IllegalArgumentException("El 'password' no puede estar vacio"));
                    }
                    return iPasswordEncoderUseCase.encode(userValidation.getPassword())
                            .map(encoded -> {
                                userValidation.setPassword(encoded);
                                return userValidation;
                            });
                });
    }
}
