package com.scc363.hospitalproject.exceptions;

public class PatientAlreadyExistsException extends Exception{
    public PatientAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
