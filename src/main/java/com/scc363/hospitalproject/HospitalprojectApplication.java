package com.scc363.hospitalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HospitalprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalprojectApplication.class, args);
    }

}

