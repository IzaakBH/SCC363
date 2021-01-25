package com.scc363.hospitalproject.security;

import com.scc363.hospitalproject.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebSecurity
public class LoginSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Tells spring to authorise all requests with basic authentication
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/h2-console/**", "/h2-console/*", "/hello", "/signin").permitAll()
                    .antMatchers("/css/**", "/js/**", "/img/**").permitAll()  //Permit access to static files
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/", "/home", "/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/signin")
                    .loginProcessingUrl("/signin")   // Where we submit the username and password to
                    .defaultSuccessUrl("/hello")
                    .failureUrl("/register.html?error=true")
                    .and()
                .logout()
                    .permitAll();

        // USed to allow access to h2-console, disable in production.
        http.headers().frameOptions().disable();
    }

    // Temp method to create a user with username user password pass and role USER
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Password storage format is specified as required in spring security 5 and above
        // Change the encoder to custom hashing method.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                    .password(passwordEncoder().encode("pass"))
//                    .roles("USER")
//                .and()
//                    .withUser("admin")
//                    .password(passwordEncoder().encode("pass"))
//                    .roles("ADMIN");
    }

    // Replace with custom password hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
