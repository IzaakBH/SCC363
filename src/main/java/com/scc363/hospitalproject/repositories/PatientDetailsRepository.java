package com.scc363.hospitalproject.repositories;

import com.scc363.hospitalproject.datamodels.PatientDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "patients", path = "patients")
public interface PatientDetailsRepository extends CrudRepository<PatientDetails, Integer> {
    PatientDetails getPatientDetailsById(int id);
    List<PatientDetails> getPatientDetailsByFirstNameAndLastName(String firstName, String lastName);
    List<PatientDetails> getPatientDetailsByDoctor(String doctor);

}
