package com.scc363.hospitalproject.datamodels;

import com.scc363.hospitalproject.constraints.UniqueEmail;
import com.scc363.hospitalproject.constraints.UniqueUsername;
import com.scc363.hospitalproject.constraints.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @UniqueUsername
    @Size(min=3, max=20)
    @Column(unique = true)
    private String username;//TODO: This HAS to be changed to a secure format. A package will exist for this.

    @ValidPassword
    private String password;

    @UniqueEmail
    @Email( message = "Email address should be valid")
    @Column(unique=true)
    private String email;

    @NotBlank(message = "Choose a user type")
    private String userType;

    @NotBlank(message = "Choose a user first name")
    private String first;

    @NotBlank(message = "Choose a user last name")
    private String last;

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String type) {
        userType = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
    @Override
    public String toString() {
        return String.format("Username: %s is a %s. Password: %s, EmailL %s]", username, userType, password, email);
    }


}
