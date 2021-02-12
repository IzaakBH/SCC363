package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.PermittedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermittedUserRepository extends JpaRepository<PermittedUser, Long>
{
    PermittedUser findPermittedUserByEmailAndUserType(String email, String userType);

}
