package com.scc363.hospitalproject.datamodels.dtos;

import com.scc363.hospitalproject.constraints.UniqueEmail;
import com.scc363.hospitalproject.constraints.UniqueUsername;
import com.scc363.hospitalproject.constraints.ValidPassword;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;


//A DTO (Data Transfer Object) is used only for transfer of the object. It implements "separation of concerns"
// See this link for a good justification for their use. https://softwareengineering.stackexchange.com/questions/373284/what-is-the-use-of-dto-instead-of-entity
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @UniqueUsername
    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String username;

    @ValidPassword
    @NotNull
    @NotEmpty
    private String password;

    @Email( message = "Email address should follow the form email@email.com")
    @Column(unique=true)
    @NotNull
    @NotEmpty
    private String email;

    @NotBlank(message = "Choose a user type")
    private String userType;

    public UserDTO() {

    }

    public UserDTO(String username, String password, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return username + " " + password + " " + email + " " + userType;
    }
}
