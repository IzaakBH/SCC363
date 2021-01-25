package com.scc363.hospitalproject.validators;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// This class is a workaround for a bug in which POST requests generate beforeCreate events but they are not called by the
//  application. See here https://www.baeldung.com/spring-data-rest-validators section 2.4
public class ValidatorEventRegister implements InitializingBean {

    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @Autowired
    private Map<String, Validator> validators;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> events = Arrays.asList("beforeCreate");
        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            events.stream().filter(p -> entry.getKey().startsWith(p)).findFirst().ifPresent(p -> validatingRepositoryEventListener.addValidator(p, entry.getValue()));
        }
    }
}
