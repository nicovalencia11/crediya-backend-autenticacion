package co.com.crediya.r2dbc.repository;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.entity.RoleEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations
        <Role, RoleEntity, Long, RoleReactiveRepository> implements RoleRepository {

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }
}
