package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.PatientDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientDetailsRepository extends CrudRepository<PatientDetails, Integer> {
    PatientDetails getPatientDetailsById(int id);
    List<PatientDetails> getPatientDetailsByFirstNameAndLastName(String firstName, String lastName);
}
