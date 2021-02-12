package com.scc363.hospitalproject.datamodels;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PermittedUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String userType;

    public PermittedUser()
    {

    }
    public PermittedUser(String email, String userType)
    {
        this.email = email;
        this.userType = userType;
    }
}
