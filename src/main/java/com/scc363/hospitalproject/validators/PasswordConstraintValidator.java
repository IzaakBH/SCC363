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
        double score = PasswordStrengthEvaluator.evaluatePassword(password);
        return score > 0.5;

    }

    public double getScore(String password, ConstraintValidatorContext context){
        return PasswordStrengthEvaluator.evaluatePassword(password);
    }
}
