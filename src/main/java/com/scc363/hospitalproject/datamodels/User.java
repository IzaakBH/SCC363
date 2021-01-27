package com.scc363.hospitalproject.datamodels;

import com.scc363.hospitalproject.Constraints.UniqueEmail;
import com.scc363.hospitalproject.Constraints.UniqueUsername;
import com.scc363.hospitalproject.Constraints.ValidPassword;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;

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

    @Override
    public String toString() {
        return String.format("Username: %s is a %s. Password: %s, EmailL %s]", username, userType, password, email);
    }


    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public void setRoles(Collection<Role> roles)
    {
        this.roles = roles;
    }

    public boolean hasRole(Role role)
    {
        for (Role assignedRole : this.roles)
        {
            if (assignedRole.getName().equals(role.getName()))
            {
                return true;
            }
        }
        return false;
    }

}
