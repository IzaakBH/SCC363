package com.scc363.hospitalproject.validators;

import com.scc363.hospitalproject.constraints.ValidPassword;
import com.scc363.hospitalproject.utils.PasswordStrengthEvaluator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword arg0) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return PasswordStrengthEvaluator.evaluatePassword(password) >= 0.5;
    }
}
