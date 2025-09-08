package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.passwordEncoder.IPasswordEncoderUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

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
}