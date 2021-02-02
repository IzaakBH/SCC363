package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.utils.CodeGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginInterface
{

    @Autowired
    private UserRepository repo;


    @Autowired
    private PasswordEncoder pEncoder;


    @Override
    public boolean isAuthenticated(String username, String password)
    {
        User user = repo.findUserByUsername(username);
        if(user != null)
        {
            System.out.println("provided password " + pEncoder.encode(password));
            System.out.println("password ONF " + user.getPassword());
            return (user.getUsername().equals(username) && pEncoder.matches(password, user.getPassword()));
        }
        else
        {
            System.out.println("no user found");
        }
        return false;
    }
}
