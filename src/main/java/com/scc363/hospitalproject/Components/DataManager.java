package com.scc363.hospitalproject.Components;

import com.scc363.hospitalproject.datamodels.Privilege;
import com.scc363.hospitalproject.datamodels.Role;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.repositories.PrivilegeRepository;
import com.scc363.hospitalproject.repositories.RoleRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class DataManager implements ApplicationListener<ContextRefreshedEvent>
{

    private boolean setup = false;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;



    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (setup)
        {
            return;
        }


        Privilege readUsers = createPrivilege("READ_USERS");
        Privilege readPatients = createPrivilege("READ_PATIENTS");


        Privilege writeUsers = createPrivilege("WRITE_USERS");
        Privilege writePatients = createPrivilege("WRITE_PATIENTS");

        Privilege updateUsers = createPrivilege("UPDATE_USERS");
        Privilege updatePatients = createPrivilege("UPDATE_PATIENTS");

        Privilege deleteUsers = createPrivilege("DELETE_USERS");
        Privilege deletePatients = createPrivilege("DELETE_PATIENTS");


        createRole("REGULATOR", Arrays.asList(readUsers, readPatients, writeUsers, writePatients, updateUsers, updatePatients, deleteUsers, deletePatients));
        createRole("SYSTEM_ADMIN", Arrays.asList(readUsers, writeUsers, updateUsers, deleteUsers));
        createRole("DOCTOR", Arrays.asList(readPatients, updatePatients));
        createRole("NURSE", Arrays.asList(readPatients, updatePatients));
        createRole("MED_ADMIN", Arrays.asList(readPatients, writePatients));
        createRole("PATIENT", Collections.singletonList(readPatients));

        User testUser = new User();
        testUser.setUsername("xavier");
        testUser.setEmail("xavierhickman1234@gmail.com");
        testUser.setPassword("password");
        testUser.setRoles(Collections.singletonList(roleRepository.findByName("REGULATOR")));
        testUser.setUserType("REGULATOR");
        userRepository.save(testUser);
        setup = true;
    }


    @Transactional
    Privilege createPrivilege(String name)
    {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null)
        {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


    @Transactional
    Role createRole(String name, Collection<Privilege> privileges)
    {
        Role role = roleRepository.findByName(name);
        if (role == null)
        {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
