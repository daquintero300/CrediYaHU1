package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.adapters.UserReactiveRepositoryAdapter;
import co.com.pragma.r2dbc.repository.UserReactiveRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    /*private final UserEntity taskEntity = UserEntity.builder()
            .id("1")
            .name("Tarea 1")
            .description("Descripción")
            .priority("LOW")
            .completed(false)
            .build();

    @Test
    void mustSaveValue() {
        when(repository.save("test")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.save("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }*/
}
