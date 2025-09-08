package co.com.pragma.usecase.auth;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import co.com.pragma.usecase.token.ITokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ITokenUseCase iTokenUseCase;

    @Mock
    private IPasswordEncoderUseCase iPasswordEncoderUseCase;

    @InjectMocks
    private AuthUserUseCase authUserUseCase;

    //
    @Test
    void testErrorIfEmailNull() {
        StepVerifier.create(authUserUseCase.login(null, "password"))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("The email cannot be null or empty"))
                .verify();
    }

    @Test
    void testErrorIfPasswordNull() {
        StepVerifier.create(authUserUseCase.login("email@test.com", null))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("The password cannot be null or empty"))
                .verify();
    }

    @Test
    void testErrorIfUserNotFound() {
        String email = "email@test.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(authUserUseCase.login(email, password))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("User does not exist"))  // asegurarte que tu código lanza este mensaje
                .verify();
    }

//    @Test
//    void errorIfPasswordIncorrect() {
//        String email = "email@test.com";
//        String password = "password";
//
//        User user = new User();
//        user.setIdRole(1L);
//        user.setPassword("encodedPassword");
//
//        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
//        when(iPasswordEncoderUseCase.matches(password, user.getPassword())).thenReturn(Mono.just(false));
//
//        StepVerifier.create(authUserUseCase.login(email, password))
//                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
//                        e.getMessage().equals("Password incorrect"))  // CAMBIO A INGLÉS para unificar con los demás
//                .verify();
//    }

    @Test
    void testErrorIfRoleNotFound() {
        String email = "email@test.com";
        String password = "password";

        User user = new User();
        user.setIdRole(1L);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(iPasswordEncoderUseCase.matches(password, user.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findRoleById(user.getIdRole())).thenReturn(Mono.empty());

        StepVerifier.create(authUserUseCase.login(email, password))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Role not found"))
                .verify();
    }

    @Test
    void testSuccessReturnsToken() {
        String email = "email@test.com";
        String password = "password";
        String expectedToken = "token123";

        User user = new User();
        user.setIdRole(1L);
        user.setPassword("encodedPassword");

        Role role = new Role();
        role.setRoleName("ADMIN");

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(iPasswordEncoderUseCase.matches(password, user.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findRoleById(user.getIdRole())).thenReturn(Mono.just(role));
        when(iTokenUseCase.createToken(user, role)).thenReturn(Mono.just(expectedToken));

        StepVerifier.create(authUserUseCase.login(email, password))
                .expectNext(expectedToken)
                .verifyComplete();
    }
}
