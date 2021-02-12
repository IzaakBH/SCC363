package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.Privilege;
import com.scc363.hospitalproject.datamodels.Role;
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
    private PasswordEncoder pEncoder;
    @Autowired
    UserManager userManager;

    @Override
    public boolean isAuthenticated(String username, String password)
    {
        User user = userManager.findUserByUsername(username);
        if(user != null)
        {
            for (Role role : user.getRoles())
            {
                System.out.println("has role " + role.getName());
                for (Privilege privilege : role.getPrivileges())
                {
                    System.out.println("With privilege " + privilege.getName());
                }
            }
            return (user.getUsername().equals(username) && pEncoder.matches(password, user.getPassword()));
        }
        return false;
    }


}
