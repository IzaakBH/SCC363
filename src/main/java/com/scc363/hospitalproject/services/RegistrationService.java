package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.utils.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegistrationService implements RegistrationInterface {
    @Autowired
    private UserRepository repo;

    /**
     * Checks that a new user does not share a username or email with an existing user before creating the use object
     * @param u The user DTO to be added
     * @return  A user object
     * @throws UserAlreadyExistsException   Exception thrown if a user already exists with the given username and password
     */
    @Override
    @Transactional
    public User registerNewUser(@javax.validation.Valid User u) throws UserAlreadyExistsException {
        if (userCredentialsExist(u.getEmail(), u.getUsername())) {
            throw new UserAlreadyExistsException("An account exists with these credentials: " + u.getUsername() + " " + u.getEmail());
        } else {
            // Convert DTO to User entity and save it.
            //return repo.save(DTOMapper.userDtoToEntity(u));
            return repo.save(u);
        }
    }

    private boolean userCredentialsExist(String email, String username) {
        System.out.println("Email and username " + email + " " + username);
        return repo.existsByEmailOrUsername(email,username);
    }
}
