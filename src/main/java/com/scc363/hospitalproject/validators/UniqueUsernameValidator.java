package com.scc363.hospitalproject.validators;

import com.scc363.hospitalproject.constraints.UniqueUsername;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    UserRepository userRepo;


    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepo.existsByUsername(username);
    }
}
