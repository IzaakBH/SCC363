package com.scc363.hospitalproject.datamodels;

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

    private Integer medicalID;

    private Integer phoneNumber;

    private String addressL1; //House number and street /name
    private String addressTown;
    private String addressPostcode;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private float weight; //KG

    private float height; //CM

    // Formats date as so: 10 April 2020.
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");

    public String getAge() {
        Period p = Period.between(LocalDate.ofInstant(dateOfBirth.toInstant(), ZoneId.systemDefault()), LocalDate.now());
        return String.format("%d years and %d days", p.getYears(), p.getDays());
    }

    public Integer getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(Integer medicalID) {
        this.medicalID = medicalID;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressL1() {
        return addressL1;
    }

    public void setAddressL1(String addressL1) {
        this.addressL1 = addressL1;
    }

    public String getAddressTown() {
        return addressTown;
    }

    public void setAddressTown(String addressTown) {
        this.addressTown = addressTown;
    }

    public String getAddressPostcode() {
        return addressPostcode;
    }

    public void setAddressPostcode(String addressPostcode) {
        this.addressPostcode = addressPostcode;
    }

    public String getDateOfBirth() {
        return dateFormatter.format(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public float getWeight() {
        return weight;
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
}
