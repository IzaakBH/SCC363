package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long>
{

    Role findByName(String name);

    @Override
    void delete(Role role);
}
