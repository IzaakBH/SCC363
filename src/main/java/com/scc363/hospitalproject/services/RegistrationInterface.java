package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;

public interface RegistrationInterface {

    User registerNewUser(@javax.validation.Valid User u) throws UserAlreadyExistsException;
}
