package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.r2dbc.entity.RoleEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.repository.RoleReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role/* change for domain model */,
        RoleEntity/* change for adapter model */,
        Long,
        RoleReactiveRepository
> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    @Transactional
    public Mono<Role> findRoleById(Long idRole) {
        System.out.println("idRole " + idRole);
        return super.findById(idRole);
    }
}