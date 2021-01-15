package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.PatientDetails;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.datamodels.UserTypes;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.LoginAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class HospitalControler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    private LoginAuthService loginAuthService;

    @PostMapping("/add")
    public String addUser(@RequestParam String userName, @RequestParam String password, @RequestParam String userType){
        User u = new User();
        u.setUsername(userName);
        u.setPassword(password);
        //TODO: Validation for non proper type here if a manual request is made.
        u.setUserType(UserTypes.valueOf(userType));
        userRepository.save(u);
        return String.format("Added %s to the database!", userName);
    }

    @GetMapping("/listusers")
    public Iterable<User> getUsers() { return userRepository.findAll(); }


}
