package com.scc363.hospitalproject.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class LoginSecurity extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Tells spring to authorise all requests with basic authentication
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .httpBasic();
        http.authorizeRequests()
                    .antMatchers("/", "/home", "/signup").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    // Temp method to create a user with username user password pass and role USER
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Password storage format is specified as required in spring security 5 and above
        auth.inMemoryAuthentication().withUser("user").password("{noop}pass").roles("USER");
    }
}
