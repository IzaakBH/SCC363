package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findUserByEmail(email);


        if (user == null) {
            throw new UsernameNotFoundException("No user exists with the email " + email);
        }
        return user;
    }

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities= new ArrayList<>();
        for (String role :roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
