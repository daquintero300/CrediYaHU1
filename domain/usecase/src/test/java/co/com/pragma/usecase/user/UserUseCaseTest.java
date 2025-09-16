package co.com.pragma.usecase.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IPasswordEncoderUseCase iPasswordEncoderUseCase;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    public void testSaveUser(){

        User user = new User("David",
                "Quintero",
                LocalDate.of(1990, 5, 14),
                "Calle 123",
                "3001234567",
                "david.quintero@gmail.com",
                BigDecimal.valueOf(3500000.00),
                1L,
                1L,
                "1234sg");

        doReturn(Mono.just(user))
                .when(userRepository).saveUser(user);

        doReturn(Mono.just("sdsdsdsdd")).when(iPasswordEncoderUseCase).encode(anyString());

        Mono<User> expectedReturn = userUseCase.saveUser(user);

        assertNotNull(expectedReturn);

        StepVerifier.create(expectedReturn)
                .expectNextMatches( expectedUser ->
                        expectedUser.getName().equals(user.getName())
                        && expectedUser.getLastName().equals(user.getLastName())
                        && expectedUser.getBirthdate().equals(user.getBirthdate())
                        && expectedUser.getAddress().equals(user.getAddress())
                        && expectedUser.getPhoneNumber().equals(user.getPhoneNumber())
                        && expectedUser.getEmail().equals(user.getEmail())
                        && expectedUser.getBaseSalary().equals(user.getBaseSalary())
                        && expectedUser.getIdNumber().equals(user.getIdNumber())
                        && expectedUser.getIdRole().equals(user.getIdRole())
                        && expectedUser.getPassword().equals(user.getPassword()))
                .verifyComplete();
    }

    @Test
    public void testValidateUser_ValidUser_ShouldReturnUserWithEncodedPassword() {

        User user = createValidUser();
        String encodedPassword = "encodedPassword123";
        
        doReturn(Mono.just(encodedPassword))
                .when(iPasswordEncoderUseCase).encode(user.getPassword());

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectNextMatches(validatedUser -> 
                    validatedUser.getName().equals(user.getName()) &&
                    validatedUser.getLastName().equals(user.getLastName()) &&
                    validatedUser.getEmail().equals(user.getEmail()) &&
                    validatedUser.getBaseSalary().equals(user.getBaseSalary()) &&
                    validatedUser.getPassword().equals(encodedPassword))
                .verifyComplete();
        
        verify(iPasswordEncoderUseCase).encode("password123");
    }

    @Test
    public void testValidateUser_NullName_ShouldReturnError() {

        User user = createValidUser();
        user.setName(null);

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'name' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_BlankName_ShouldReturnError() {

        User user = createValidUser();
        user.setName("   ");

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'name' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_NullLastName_ShouldReturnError() {

        User user = createValidUser();
        user.setLastName(null);

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'lastName' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_BlankLastName_ShouldReturnError() {

        User user = createValidUser();
        user.setLastName("   ");

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'lastName' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_NullEmail_ShouldReturnError() {

        User user = createValidUser();
        user.setEmail(null);

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'email' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_BlankEmail_ShouldReturnError() {

        User user = createValidUser();
        user.setEmail("   ");

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'email' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_InvalidEmailFormat_ShouldReturnError() {

        User user = createValidUser();
        user.setEmail("invalid-email-format");

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El formato del correo no es válido"))
                .verify();
    }

    @Test
    public void testValidateUser_NullBaseSalary_ShouldReturnError() {

        User user = createValidUser();
        user.setBaseSalary(null);

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'baseSalary' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_NegativeBaseSalary_ShouldReturnError() {

        User user = createValidUser();
        user.setBaseSalary(BigDecimal.valueOf(-1000));

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El salario debe estar entre 0 y 15.000.000"))
                .verify();
    }

    @Test
    public void testValidateUser_BaseSalaryExceedsLimit_ShouldReturnError() {

        User user = createValidUser();
        user.setBaseSalary(BigDecimal.valueOf(15000001));

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El salario debe estar entre 0 y 15.000.000"))
                .verify();
    }

    @Test
    public void testValidateUser_BaseSalaryAtMinimum_ShouldPass() {

        User user = createValidUser();
        user.setBaseSalary(BigDecimal.ZERO);
        String encodedPassword = "encodedPassword123";
        
        doReturn(Mono.just(encodedPassword))
                .when(iPasswordEncoderUseCase).encode(user.getPassword());

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectNextMatches(validatedUser -> 
                    validatedUser.getBaseSalary().equals(BigDecimal.ZERO))
                .verifyComplete();
    }

    @Test
    public void testValidateUser_BaseSalaryAtMaximum_ShouldPass() {

        User user = createValidUser();
        user.setBaseSalary(new BigDecimal("15000000"));
        String encodedPassword = "encodedPassword123";
        
        doReturn(Mono.just(encodedPassword))
                .when(iPasswordEncoderUseCase).encode(user.getPassword());

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectNextMatches(validatedUser -> 
                    validatedUser.getBaseSalary().equals(new BigDecimal("15000000")))
                .verifyComplete();
    }

    @Test
    public void testValidateUser_NullPassword_ShouldReturnError() {

        User user = createValidUser();
        user.setPassword(null);

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'password' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_BlankPassword_ShouldReturnError() {

        User user = createValidUser();
        user.setPassword("   ");

        Mono<User> result = userUseCase.validateUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().equals("El 'password' no puede estar vacio"))
                .verify();
    }

    @Test
    public void testValidateUser_ValidEmailFormats_ShouldPass() {

        String[] validEmails = {
            "test@example.com",
            "user.name@domain.co",
            "user123@test-domain.org",
            "user@domain.co"
        };
        
        String encodedPassword = "encodedPassword123";
        doReturn(Mono.just(encodedPassword))
                .when(iPasswordEncoderUseCase).encode(anyString());

        for (String email : validEmails) {
            User user = createValidUser();
            user.setEmail(email);

            Mono<User> result = userUseCase.validateUser(user);

            StepVerifier.create(result)
                    .expectNextMatches(validatedUser -> 
                        validatedUser.getEmail().equals(email))
                    .verifyComplete();
        }
    }

    private User createValidUser() {
        return new User(
            "John",
            "Doe",
            LocalDate.of(1990, 5, 14),
            "Calle 123",
            "3001234567",
            "john.doe@example.com",
            BigDecimal.valueOf(3500000.00),
            1L,
            1L,
            "password123"
        );
    }
}