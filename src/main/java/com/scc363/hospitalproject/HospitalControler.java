package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.MultiFactorAuthCodeGen;
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

    private User u;



    @PostMapping("/add")
    public String addUser(@RequestParam String first, @RequestParam String last, @RequestParam String email, @RequestParam String userName, @RequestParam String password, @RequestParam String userType){
        u = new User();
        u.setUsername(userName);
        u.setPassword(password);
        u.setUserEmail(email);
        u.setFirstName(first);
        u.setLastName(last);
        //TODO: Validation for non proper type here if a manual request is made.
        u.setUserType(UserTypes.valueOf(userType));
        userRepository.save(u);
        return String.format("Added %s to the database!", userName);
    }

    @GetMapping("/listusers")
    public Iterable<User> getUsers() { return userRepository.findAll(); }


        @GetMapping("/login")
    public boolean login(@RequestParam String userName, @RequestParam String password) {
        try {
            User u1 = userRepository.findUserByUsername(userName);
            if(u1.getPassword() == null) {
                u1.sendEmail(u1.getUserEmail());
                return true;
            }
            else{
                System.out.println(u1.getPassword());
                return false;
            }
        }catch(Exception e){
            System.out.println("Not found");
            return true;
            }
    }


    @GetMapping("/code")
    public boolean checkCode(@RequestParam int code,  @RequestParam String userName) {
        try {
            u = userRepository.findUserByUsername(userName);
            if(code==1){
                return true;
            }
            else{
                System.out.println("wrong code");
                return false;
            }
        }catch(Exception e){
            System.out.println("Not found");
            return false;
        }
    }

}
