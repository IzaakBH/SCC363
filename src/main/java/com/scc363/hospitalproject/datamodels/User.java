package com.scc363.hospitalproject.datamodels;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;

import com.scc363.hospitalproject.constraints.UniqueEmail;
import com.scc363.hospitalproject.constraints.UniqueUsername;
import com.scc363.hospitalproject.constraints.ValidPassword;
import com.scc363.hospitalproject.services.CodeGen;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @UniqueUsername
    @Size(min=3, max=20)
    @Column(unique = true)
    private String username;//TODO: This HAS to be changed to a secure format. A package will exist for this.

    @ValidPassword
    private String password;

    @UniqueEmail
    @Email( message = "Email address should be valid")
    @Column(unique=true)
    private String email;

    @NotBlank(message = "Choose a user type")
    private String userType;

    @NotBlank(message = "Choose a user first name")
    private String first;

    @NotBlank(message = "Choose a user last name")
    private String last;

    String code;
    // Getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
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
