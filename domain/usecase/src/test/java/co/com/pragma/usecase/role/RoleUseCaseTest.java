package co.com.pragma.usecase.role;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleUseCase roleUseCase;

    @Test
    public void testFindRoleById() {

        Long id = 3L;
        String name = "CUSTOMER";
        String description = "Client role with basic access to the application";

        Role mockedRole = Mockito.mock(Role.class);
        doReturn(name).when(mockedRole).getRoleName();
        doReturn(description).when(mockedRole).getRoleDescription();
        doReturn(Mono.just(mockedRole)).when(roleRepository).findRoleById(id);

        Mono<Role> expectedReturn = roleUseCase.findRoleById(id);

        assertNotNull(expectedReturn);
        StepVerifier.create(expectedReturn)
                .expectNextMatches( expectedRole ->
                        expectedRole.getRoleName().equals(name) && expectedRole.getRoleDescription().equals(description))
                .verifyComplete();
    }
}