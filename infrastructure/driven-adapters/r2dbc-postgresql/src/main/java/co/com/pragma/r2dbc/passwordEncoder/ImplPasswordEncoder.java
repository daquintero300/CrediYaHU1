package co.com.pragma.r2dbc.passwordEncoder;

import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ImplPasswordEncoder implements IPasswordEncoderUseCase {


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Mono<String> encode(String password) {
        return Mono.just(encoder.encode(password));
    }

    @Override
    public Mono<Boolean> matches(String password, String encodedPassword) {
        return Mono.just(encoder.matches(password, encodedPassword));
    }
}
