package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserById(Integer id);
    User findUserByUsername(String username);
    User findUserByEmailAndUsername(String email, String username);
    User findUserByEmail(String email);
    long deleteUserById(int id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailOrUsername(String email, String username);
    boolean existsByEmailAndCode(String email, int code);


}
