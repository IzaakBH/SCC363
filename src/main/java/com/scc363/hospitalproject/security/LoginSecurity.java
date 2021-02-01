package com.scc363.hospitalproject.security;

import com.scc363.hospitalproject.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

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
                    .antMatchers("/", "/home", "/register", "/verify-account", "/verifymessage", "/verified", "/notverified").permitAll()
                    .antMatchers("/h2-console/**", "/h2-console/*", "/signin", "/hello").permitAll()
                    .antMatchers("/js/**", "/css/**", "/img/**").permitAll()  //Permit access to static files
                    .antMatchers("/test/**").hasRole("SYSADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/signin").permitAll()
                    .and()
                .logout()
                    .permitAll();

        // USed to allow access to h2-console, disable in production.
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
