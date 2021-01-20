package com.scc363.hospitalproject.ExceptionHandlers;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

// When invalid user details are validated, a RepositoryConstraintViolationException is thrown. This class handles that.
// See section 3, https://www.baeldung.com/spring-data-rest-validators
public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ RepositoryConstraintViolationException.class })
    public ResponseEntity<Object> handleAccessDeniedException (Exception e, WebRequest req){
        RepositoryConstraintViolationException nevEx = (RepositoryConstraintViolationException) e;

        String errors = nevEx.getErrors().getAllErrors().stream().map(p -> p.toString()).collect(Collectors.joining("\n"));

        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.PARTIAL_CONTENT);
    }


}
