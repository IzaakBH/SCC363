package com.scc363.hospitalproject.datamodels;

import javax.persistence.*;
import java.util.ArrayList;
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

    public boolean hasPrivileges(Privilege privilege) {
        for (Privilege assignedPrivilege : this.privileges)
        {
            if (assignedPrivilege.getName().equals(privilege.getName()))
            {
                return true;
            }
        }
        return false;
    }

    public Collection<Privilege> getPrivileges()
    {
        return this.privileges;
    }

}
