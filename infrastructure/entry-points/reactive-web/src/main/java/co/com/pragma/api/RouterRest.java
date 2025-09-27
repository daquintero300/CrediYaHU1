package co.com.pragma.api;

import co.com.pragma.api.config.JwtAuthenticationFilter;
import co.com.pragma.api.openapi.AuthenticationOpenApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserHandler userHandler;
    private final AuthHandler authHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouterFunction<ServerResponse> userRouterFunction() {
        RouterFunction<ServerResponse> loginUser = route()
                .POST("/api/v1/login", authHandler::listenLoginUser, AuthenticationOpenApi::loginUser)
                .build();
        RouterFunction<ServerResponse> saveUser = route()
                .POST("/api/v1/usuarios" ,userHandler::listenSaveUser, AuthenticationOpenApi::saveUser)
                .build()
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADMIN","ADVISOR")));
        RouterFunction<ServerResponse> findAllUsers = route()
                .GET("/api/v1/usuarios", userHandler::listenFindAllUsers, AuthenticationOpenApi::findAllUsers)
                .build()
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADVISOR")));
        RouterFunction<ServerResponse> findUserByEmail = route()
                .GET("/api/v1/usuarios/findUserByEmail", userHandler::listenFindUserByEmail, AuthenticationOpenApi::findUserByEmail)
                .build()
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADVISOR","CUSTOMER")));
        return saveUser.and(loginUser).and(findAllUsers).and(findUserByEmail);
    }
}
