package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.PermittedUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PermittedUsersRepository extends CrudRepository<PermittedUser, Integer>
{
    PermittedUser getPermittedUserById(Integer id);
    PermittedUser getPermittedUserByEmailAndUserType(String email, String userType);

}
