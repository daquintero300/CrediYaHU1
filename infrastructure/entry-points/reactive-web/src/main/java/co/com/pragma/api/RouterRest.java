package co.com.pragma.api;

import co.com.pragma.api.config.JwtAuthenticationFilter;
import co.com.pragma.api.dto.request.UserDTORequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserHandler userHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE
                    },
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "listenSaveUser",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User created successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = UserDTORequest.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid request"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userhandler,
                                                             AuthHandler authHandler,
                                                             JwtAuthenticationFilter jwtAuthenticationFilter) {
        RouterFunction<ServerResponse> saveUser = route(POST("/api/v1/usuarios"), userhandler::listenSaveUser)
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADMIN","ADVISOR")));
        RouterFunction<ServerResponse> loginUser = route(POST("/api/v1/login"), authHandler::listenLoginUser);
        RouterFunction<ServerResponse> findAllUsers = route(GET("/api/v1/usuarios"), userhandler::listenFinAllUsers)
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADVISOR")));
        RouterFunction<ServerResponse> findUserByEmail = route(GET("/api/v1/usuarios/findUserByEmail"), userhandler::listenFinUserByEmail)
                .filter(jwtAuthenticationFilter.requireRole(List.of("ADVISOR","CUSTOMER")));

        return saveUser.and(loginUser).and(findAllUsers).and(findUserByEmail);
    }
}
