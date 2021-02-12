package com.scc363.hospitalproject.datamodels;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;

import com.scc363.hospitalproject.constraints.UniqueUsername;
import com.scc363.hospitalproject.constraints.ValidPassword;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Collections;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @UniqueUsername
    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String username;

    @ValidPassword
    @NotNull
    @NotEmpty
    private String password;

    @Email( message = "Email address should follow the form email@email.com")
    @Column(unique=true)
    @NotNull
    @NotEmpty
    private String email;

    @NotBlank(message = "Choose a user type")
    private String userType;

    @NotBlank(message = "Choose a user first name")
    private String first;

    @NotBlank(message = "Choose a user last name")
    private String last;

    private String code;

    private boolean locked;

    // Account is enabled if it has been verified with the email 2fa code.
    private boolean enabled;


    @Column
    private Integer activationAttempts = 0;

    public Integer getActivationAttempts()
    {
        return this.activationAttempts;
    }

    public void addActivationAttempt()
    {
        if (this.activationAttempts < 3) this.activationAttempts++;
    }

    public boolean isLocked()
    {
        return this.locked;
    }





    public void lockAccount()
    {
        this.locked = true;
    }

    public void unlockAccount()
    {
        this.locked = false;
        this.activationAttempts = 0;
    }

    public User() {
        this.enabled = false;
        this.locked = false;
    }

    public User(String username, String password, String email, String userType, String first, String last) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.first = first;
        this.last = last;
        this.enabled = false;
        this.locked = false;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() { return code; }

    public void setCode(int code) {
        this.code = String.valueOf(code);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String type) {
        userType = type;
    }

    public Integer getId()
    {
        return this.id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority(userType);
        return Collections.singletonList(sga);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
    @Override
    public String toString() {
        return String.format("Username: %s is a %s. Password: %s, EmailL %s]", username, userType, password, email);
    }

    public void sendEmail(String to){
        String from = "scc363gr@gmail.com";
        String password = "SCC363group";
        String sub = "Code";


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText("http://localhost:8080/verify-account?email=" + getEmail() + "&token=" + getCode());
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }


    @Transient
    private ArrayList<Role> roles;

    public void setRoles(ArrayList<Role> roles)
    {
        this.roles = roles;
    }

    public ArrayList<Role> getRoles()
    {
        return this.roles;
    }

    public boolean hasRole(Role role)
    {
        for (Role assignedRole : this.roles)
        {
            if (assignedRole.getName().equals(role.getName()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        for (Role assignedRole : this.roles)
        {
            if (assignedRole.hasPrivileges(privilege))
            {
                return true;
            }
        }
        return false;
    }

}

