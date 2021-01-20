package com.scc363.hospitalproject.Constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordConstraintValidator eval = new PasswordConstraintValidator();

    }
}
