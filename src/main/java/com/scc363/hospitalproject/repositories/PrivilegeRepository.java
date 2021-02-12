package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long>
{

    Privilege findByName(String privilege);

    @Override
    void delete(Privilege privilege);
}
