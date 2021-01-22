package com.scc363.hospitalproject.constraints;

import com.scc363.hospitalproject.validators.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UniqueEmail {

    public String message() default "Email already registered";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};
}
