package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>
{

    Role findRoleByName(String name);

    @Override
    void delete(Role role);
}
