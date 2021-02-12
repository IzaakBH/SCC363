package com.scc363.hospitalproject.datamodels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
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

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @NotBlank
    @Column(unique = true)
    private String medicalID;

    @NotNull
    private Long phoneNumber;
    @NotBlank
    private String address;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @NotNull
    private Integer weight; //KG

    @NotNull
    private Integer height; //CM
    @NotBlank
    private String doctor;

    public PatientDetails() {

    }

    public PatientDetails(String firstName, String lastName, String medicalID, String phoneNumber, String address, String date, Integer weight, Integer height, String doctor) throws ParseException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.medicalID = medicalID;
        this.phoneNumber = Long.parseLong(phoneNumber);
        this.address = address;
        this.dateOfBirth = dateFormatter.parse(date);
        this.weight = weight;
        this.height = height;
        this.doctor = doctor;
    }

    // Formats date as so: 10 April 2020.
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");

    public int getID()
    {
        return this.id;
    }
    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
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

    public Integer getWeight() {
        return weight;
    }

    public String getDoctor()
    {
        return doctor;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
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
