package com.scc363.hospitalproject.validators;

import com.scc363.hospitalproject.constraints.UniqueEmail;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepo.existsByEmail(email);
    }
}
