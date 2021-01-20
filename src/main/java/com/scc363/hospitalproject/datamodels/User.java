package com.scc363.hospitalproject.datamodels;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Properties;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    //TODO: This HAS to be changed to a secure format. A package will exist for this.
    private String password;

    private UserTypes userType;

    private String email;

    private String first;

    private String last;

    //private MultiFactorAuthCodeGen code;

    private float sentCode;

    public String getFirstName() {
        return first;
    }

    public void setFirstName(String first) {
        this.first = first;
    }

    public String getLastName() {
        return last;
    }

    public void setLastName(String last) {
        this.last = last;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public UserTypes getUserType() {
        return userType;
    }

    public void setUserType(UserTypes type) {
        userType = type;
    }

    public String setPassword(String password) {
        return password;
    }

    public void sendEmail(){

        MultiFactorAuthCodeGen code = new MultiFactorAuthCodeGen();
        sentCode = code.getCode();

        String to = email;//change accordingly
        String from = "maria.ntemiri.mn@gmail.com";//change accordingly
        String host = "localhost";//or IP address

        //Get the session object
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        //compose the message
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Ping");
            message.setText("Hello, the code is:  " + sentCode);

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");

        }catch (MessagingException mex) {mex.printStackTrace();}
    }


    public float getCode(){
        return sentCode;
    }

    @Override
    public String toString() {
        return String.format("Username: %s is a %s]", username, userType.toString());
    }

//    public User(){
//        code = new MultiFactorAuthCodeGen();
//        sentCode = code.getCode();
//    }
}
