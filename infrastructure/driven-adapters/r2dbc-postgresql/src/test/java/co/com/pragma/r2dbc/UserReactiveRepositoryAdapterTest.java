package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
