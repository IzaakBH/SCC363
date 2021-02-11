package com.scc363.hospitalproject.Components;

import com.scc363.hospitalproject.datamodels.PatientDetails;
import com.scc363.hospitalproject.datamodels.Privilege;
import com.scc363.hospitalproject.datamodels.Role;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.PrivilegeRepository;
import com.scc363.hospitalproject.repositories.RoleRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


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


        createRole("REGULATOR", Arrays.asList(readUsers, readPatients, updateUsers, updatePatients));
        createRole("SYSTEM_ADMIN", Arrays.asList(readUsers, writeUsers, updateUsers, deleteUsers));
        createRole("DOCTOR", Arrays.asList(readPatients, updatePatients));
        createRole("NURSE", Arrays.asList(readPatients, updatePatients));
        createRole("MED_ADMIN", Arrays.asList(readPatients, writePatients, deletePatients));
        createRole("PATIENT", Collections.singletonList(readPatients));


        User sysAdmin = new User();
        sysAdmin.setUsername("xavier");
        sysAdmin.setPassword(passwordEncoder.encode("##PPassword123"));
        sysAdmin.setUserType("SYSTEM_ADMIN");
        sysAdmin.setEmail("xavierhickman1234@gmail.com");
        sysAdmin.setEnabled(true);
        sysAdmin.setFirst("xavier");
        sysAdmin.setLast("hickman");
        userRepository.save(sysAdmin);

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
        Role role = roleRepository.findRoleByName(name);
        if (role == null)
        {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
