package com.scc363.hospitalproject.Constraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)  // Indicates the validation is implemented by the UniqueUsernameValidator
@Target({ FIELD })  //Indicates where the annotation is applied (field, class, method)
@Retention(RUNTIME)  // indicates how long the annotation will be making impact on the code (before or after compilation) This annotation is available after runtime
public @interface UniqueUsername {

    // Returns default key for creating error messages
    public String message() default "Username already exists";

    // Allows us to specify validation groups for constraints
    public Class<?>[] groups() default {};

    // Can be used by clients of the bean validation api to assign custom payload objects to a constraint.
    public Class<? extends Payload>[] payload() default{};
}
