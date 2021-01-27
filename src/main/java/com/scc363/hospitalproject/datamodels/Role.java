package com.scc363.hospitalproject.datamodels;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    public Role()
    {
        super();
    }


    public Role(String name)
    {
        super();
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;


    public void setPrivileges(Collection<Privilege> privileges)
    {
        this.privileges = privileges;
    }

}
