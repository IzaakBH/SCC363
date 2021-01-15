package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserById(Integer id);
    User findUserByUsername(String username);
    long deleteUserById(int id);
}
