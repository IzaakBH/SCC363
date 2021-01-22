package com.scc363.hospitalproject.datamodels;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    //TODO: This HAS to be changed to a secure format. A package will exist for this.
    private String password;

    private UserTypes userType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes type) {
        userType = type;
    }

    public String setPassword(String password) {
        return password;
    }

    @Override
    public String toString() {
        return String.format("Username: %s is a %s]", username, userType.toString());
    }


}
