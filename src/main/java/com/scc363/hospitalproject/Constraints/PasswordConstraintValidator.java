package com.scc363.hospitalproject.Constraints;

import com.scc363.hospitalproject.utils.PasswordStrengthEvaluator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        double score = PasswordStrengthEvaluator.evaluatePassword(password);
        return score < 0.7;

    }
}
