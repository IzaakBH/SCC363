package com.scc363.hospitalproject.datamodels;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;

import com.scc363.hospitalproject.constraints.UniqueUsername;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.scc363.hospitalproject.services.CodeGen;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Collections;


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

    //@ValidPassword
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

    String code;
    private boolean locked;


    public User() {

    }

    public User(String username, String password, String email, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() { return code; }

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
        return true;
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
        CodeGen c = new CodeGen();
        String msg = String.valueOf(c.generateCode());
        code = msg;


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
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
            message.setText(msg);
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }


}

enum UserTypes {
    REGULATOR,
    SYSADMIN,
    DOCTOR,
    NURSE,
    MEDADMIN,
    PATIENT
}
