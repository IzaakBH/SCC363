package com.scc363.hospitalproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
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
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/", "/home", "/signup").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    //.loginPage("/login")
                    .loginProcessingUrl("/perform_login")   // Where we submit the username and password to
                    .defaultSuccessUrl("/hello")
                    .failureUrl("/login.html?error=true")
                    .and()
                .logout()
                    .permitAll();
    }

    // Temp method to create a user with username user password pass and role USER
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Password storage format is specified as required in spring security 5 and above
        // Change the encoder to custom hashing method.
        auth.inMemoryAuthentication()
                .withUser("user")
                    .password(passwordEncoder().encode("pass"))
                    .roles("USER")
                .and()
                    .withUser("admin")
                    .password(passwordEncoder().encode("pass"))
                    .roles("ADMIN");
    }

    // Replace with custom password hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
