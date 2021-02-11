package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.Role;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.repositories.RoleRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Service
public class UserManager implements UserManagerInterface
{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public User findUserByUsername(String username)
    {
        User user = userRepository.findUserByUsername(username);
        if (user != null)
        {
            user.setRoles(new ArrayList<Role>(Collections.singleton(roleRepository.findByName(user.getUserType()))));
        }
        return user;
    }
}
