package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.PatientDetails;
import com.scc363.hospitalproject.exceptions.PatientAlreadyExistsException;

public interface PatientInterface {
    PatientDetails registerPatient(@javax.validation.Valid PatientDetails p) throws PatientAlreadyExistsException;
}
