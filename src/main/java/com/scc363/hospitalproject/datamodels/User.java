package com.scc363.hospitalproject.datamodels;

import com.sun.mail.smtp.SMTPTransport;

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

    private String code;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {return password;}

   //public float getCode(){ return Integer.valueOf(code);}

    public Object sendEmail(String email) {
        MultiFactorAuthCodeGen codeGen = new MultiFactorAuthCodeGen();
        code = String.valueOf(codeGen.getCode());

        // Recipient's email ID needs to be mentioned.
        String to = "maria.ntemiri.mn@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "maria.ntemiri.mn@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, "*******");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return mex.toString();
        }
        return "success";
    }


    @Override
    public String toString() {
        return String.format("Username: %s is a %s]", username, userType.toString());
    }


}
