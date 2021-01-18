package com.scc363.hospitalproject.validators;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.datamodels.UserTypes;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateUserValidator") //Tells spring to run these methods before adding a user to the DB for validation
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
        User user = (User) obj;

        System.out.println(user.toString());
        if (checkInputString(user.getUsername())) {
            errors.rejectValue("name", "name.empty");
        }

        if (checkInputString(user.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }

        if (checkInputString(user.getUserType())) {
            errors.rejectValue("userType", "userType.empty");
        }

        if (checkInputString(user.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }

    // Checks that the userType is valid (exists in the UserTypes enum)
    private boolean checkInputEnum(String input) {
        for (UserTypes t : UserTypes.values()){
            if (t.name().equals(input)){
                return false;
            }
        }

        // Not a valid type, returns false
        return true;
    }
}
