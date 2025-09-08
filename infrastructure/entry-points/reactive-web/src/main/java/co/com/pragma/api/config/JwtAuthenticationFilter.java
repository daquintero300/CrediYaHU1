package co.com.pragma.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("➡️ Iniciando filtro de autenticación para path: {}", exchange.getRequest().getURI().getPath());
        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/api/v1/login") || path.startsWith("/webjars/swagger-ui")
                || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui.html")
                || path.startsWith("/actuator")) {
            log.info("Ruta excluida del filtro de autenticación: {}", path);
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("❌ Token de autorización inválido o ausente");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.error(new Exception("Token de autorización inválido"));
        }
        String token = authHeader.substring(7);
        return Mono.fromCallable(() ->
                        Jwts.parser()
                                .setSigningKey(privateKey.getBytes())
                                .parseClaimsJws(token)
                                .getBody()
                )
                .map(claims -> {
                    log.debug("✅ Token válido. Claims extraídos: {}", claims);
                    exchange.getAttributes().put("claims", claims);
                    return claims;
                })
                .flatMap(claims -> chain.filter(exchange));
    }

    public Mono<Boolean> hasRole(ServerRequest request, List<String> allowedRoles) {
        Object claimsAttr = request.exchange().getAttribute("claims");
        if (!(claimsAttr instanceof Claims claims)) {
            log.warn("❌ Claims no encontrados o inválidos en la solicitud");
            return Mono.just(false);
        }
        String userRole = claims.get("role", String.class);
        String user = claims.get("name", String.class);
        log.info("➡️ Verificando rol del usuario {}: {}", user, userRole);
        return Mono.just(userRole != null && allowedRoles.contains(userRole));
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> requireRole(List<String> allowedRoles) {
        return (request, next) ->
                hasRole(request, allowedRoles)
                        .flatMap(hasRole -> {
                            if (hasRole) {
                                log.info("✅ Acceso permitido para roles: {}", allowedRoles);
                                return next.handle(request);
                            } else {
                                log.warn("❌ Acceso denegado para roles: {}", allowedRoles);
                                return Mono.error(new Exception("No tienes permiso para acceder a este recurso"));
                            }
                        });
    }
}
