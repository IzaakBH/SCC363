package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.PatientDetails;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.exceptions.PatientAlreadyExistsException;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class PatientService implements PatientInterface{
    @Autowired
    private PatientDetailsRepository repo;

    @Override
    public PatientDetails registerPatient(@Valid PatientDetails p) throws PatientAlreadyExistsException {
        if (patientExists(p.getMedicalID())) {
            throw new PatientAlreadyExistsException("A patient already exists with medical id " + p.getMedicalID());
        } else {
            return repo.save(p);
        }
    }

    private boolean patientExists(String medicalID) {
        return repo.existsByMedicalID(medicalID);
    }
}
