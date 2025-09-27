package co.com.pragma.r2dbc.jwtToken;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.token.ITokenUseCase;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils implements ITokenUseCase{

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.expiration}")
    private int expirationToken;

    @Value("${security.jwt.user-generator}")
    private String userGenerator;



    @Override
    public Mono<Map<String, String>> createToken(User user, Role role) {

    String jwt = Jwts.builder()
            .setSubject(user.getEmail())
            .claim("name", user.getName())
            .claim("role", role.getRoleName())
            .setIssuer(this.userGenerator)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + this.expirationToken))
            .setNotBefore(new Date(System.currentTimeMillis()))
            .setId(UUID.randomUUID().toString())
            .signWith(Keys.hmacShaKeyFor(this.privateKey.getBytes(StandardCharsets.UTF_8)),
                    SignatureAlgorithm.HS256)
            .compact();
        return Mono.just(Map.of("token", jwt));
    }
}
