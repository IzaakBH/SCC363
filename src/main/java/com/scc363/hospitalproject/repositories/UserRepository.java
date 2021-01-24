package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserById(Integer id);
    User findUserByUsername(String username);
    User findUserByEmailAndUsername(String email, String username);
    User findUserByEmail(String email);
    long deleteUserById(int id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailOrUsername(String email, String username);
}
