package co.com.pragma.api.openapi;

import co.com.pragma.api.dto.request.AuthLoginDTORequest;
import co.com.pragma.api.dto.request.UserDTORequest;
import co.com.pragma.api.dto.response.UserDTOResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.core.fn.builders.securityrequirement.Builder.securityRequirementBuilder;

@UtilityClass
public class AuthenticationOpenApi {

    private final String SUCCESS = "Success";
    private final String SUCCESS_CODE = String.valueOf(HttpStatus.OK.value());
    private final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private final String BAD_REQUEST = HttpStatus.BAD_REQUEST.getReasonPhrase();
    private final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private final String UNAUTHORIZED = HttpStatus.UNAUTHORIZED.getReasonPhrase();
    private final String UNAUTHORIZED_CODE = String.valueOf(HttpStatus.UNAUTHORIZED.value());
    private final String FORBIDDEN = HttpStatus.FORBIDDEN.getReasonPhrase();
    private final String FORBIDDEN_CODE = String.valueOf(HttpStatus.FORBIDDEN.value());
    private final String INTERNAL_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    private final String INTERNAL_ERROR_CODE = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    private final String JWT_SECURITY = "Bearer Authentication";

    public Builder loginUser(Builder builder) {
        return builder
                .operationId("loginUser")
                .summary("Iniciar sesión")
                .description("Autentica un usuario con email y contraseña, devuelve un token JWT")
                .tag("Autenticación")
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(AuthLoginDTORequest.class))))

                // Respuesta exitosa (200)
                .response(responseBuilder()
                        .responseCode(SUCCESS_CODE)
                        .description("Login exitoso")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(AuthLoginDTORequest.class))))

                // Error 400 - Credenciales inválidas
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description("Credenciales inválidas o datos faltantes")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 401 - No autorizado
                .response(responseBuilder()
                        .responseCode(UNAUTHORIZED_CODE)
                        .description("Email o contraseña incorrectos")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 500 - Error interno
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))));
    }

    public Builder saveUser(Builder builder) {
        return builder
                .operationId("saveUser")
                .summary("Crear nuevo usuario")
                .description("Registra un nuevo usuario en el sistema. Requiere rol ADMIN o ADVISOR.")
                .tag("Usuarios")
                .security(securityRequirementBuilder().name(JWT_SECURITY))
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UserDTORequest.class))))

                // Respuesta exitosa (201)
                .response(responseBuilder()
                        .responseCode(CREATED_CODE)
                        .description("Usuario creado exitosamente")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UserDTOResponse.class))))

                // Error 400 - Datos inválidos
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description("Datos de usuario inválidos o email ya existe")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 401 - No autorizado
                .response(responseBuilder()
                        .responseCode(UNAUTHORIZED_CODE)
                        .description("Token de autenticación inválido o expirado")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 403 - Prohibido
                .response(responseBuilder()
                        .responseCode(FORBIDDEN_CODE)
                        .description("Acceso denegado. Se requiere rol ADMIN o ADVISOR")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 500 - Error interno
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))));
    }

    public Builder findAllUsers(Builder builder) {
        return builder
                .operationId("findAllUsers")
                .summary("Obtener todos los usuarios")
                .description("Obtiene la lista completa de usuarios registrados. Requiere rol ADVISOR.")
                .tag("Usuarios")
                .security(securityRequirementBuilder().name(JWT_SECURITY))

                // Parámetros de consulta opcionales
                .parameter(parameterBuilder()
                        .name("page")
                        .description("Número de página (comenzando en 0)")
                        .in(ParameterIn.QUERY)
                        .required(false)
                        .schema(schemaBuilder().implementation(Integer.class))
                        .example("0"))

                .parameter(parameterBuilder()
                        .name("size")
                        .description("Tamaño de página")
                        .in(ParameterIn.QUERY)
                        .required(false)
                        .schema(schemaBuilder().implementation(Integer.class))
                        .example("10"))

                .parameter(parameterBuilder()
                        .name("role")
                        .description("Filtrar por rol")
                        .in(ParameterIn.QUERY)
                        .required(false)
                        .schema(schemaBuilder().implementation(String.class))
                        .example("CUSTOMER"))

                // Respuesta exitosa (200)
                .response(responseBuilder()
                        .responseCode(SUCCESS_CODE)
                        .description("Lista de usuarios obtenida exitosamente")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UserDTOResponse[].class))))

                // Error 401 - No autorizado
                .response(responseBuilder()
                        .responseCode(UNAUTHORIZED_CODE)
                        .description("Token de autenticación inválido o expirado")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 403 - Prohibido
                .response(responseBuilder()
                        .responseCode(FORBIDDEN_CODE)
                        .description("Acceso denegado. Se requiere rol ADVISOR")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 500 - Error interno
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))));
    }

    public Builder findUserByEmail(Builder builder) {
        return builder
                .operationId("findUserByEmail")
                .summary("Buscar usuario por email")
                .description("Busca un usuario específico por su dirección de email. Requiere rol ADVISOR o CUSTOMER.")
                .tag("Usuarios")
                .security(securityRequirementBuilder().name(JWT_SECURITY))

                // Parámetro de consulta requerido
                .parameter(parameterBuilder()
                        .name("email")
                        .description("Email del usuario a buscar")
                        .in(ParameterIn.QUERY)
                        .required(true)
                        .schema(schemaBuilder().implementation(String.class))
                        .example("usuario@ejemplo.com"))

                // Respuesta exitosa (200)
                .response(responseBuilder()
                        .responseCode(SUCCESS_CODE)
                        .description("Usuario encontrado exitosamente")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(UserDTOResponse.class))))

                // Error 400 - Email inválido
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description("Email inválido o no proporcionado")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 401 - No autorizado
                .response(responseBuilder()
                        .responseCode(UNAUTHORIZED_CODE)
                        .description("Token de autenticación inválido o expirado")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 403 - Prohibido
                .response(responseBuilder()
                        .responseCode(FORBIDDEN_CODE)
                        .description("Acceso denegado. Se requiere rol ADVISOR o CUSTOMER")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 404 - No encontrado
                .response(responseBuilder()
                        .responseCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .description("Usuario no encontrado con el email proporcionado")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))))

                // Error 500 - Error interno
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ErrorResponse.class))));
    }
}