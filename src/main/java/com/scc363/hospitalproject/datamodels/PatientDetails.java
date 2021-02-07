package com.scc363.hospitalproject.datamodels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
public class PatientDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    private String medicalID;

    private Integer phoneNumber;

    private String address;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private float weight; //KG

    private float height; //CM
    private String doctor;



    // Formats date as so: 10 April 2020.
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");

    public String getAge() {
        Period p = Period.between(LocalDate.ofInstant(dateOfBirth.toInstant(), ZoneId.systemDefault()), LocalDate.now());
        return String.format("%d years and %d days", p.getYears(), p.getDays());
    }

    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public float getWeight() {
        return weight;
    }

    public String getDoctor()
    {
        return doctor;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDoctor(String doctor)
    {
        this.doctor = doctor;
    }


}
