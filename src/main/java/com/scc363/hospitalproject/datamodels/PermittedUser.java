package com.scc363.hospitalproject.datamodels;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
public class PermittedUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String email;

    @Column
    private String userType;


    public PermittedUser(String email, String userType)
    {
        super();
        this.email = email;
        this.userType = userType;
    }
}
