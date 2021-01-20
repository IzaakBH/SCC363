package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.PatientDetails;
import com.scc363.hospitalproject.datamodels.User;
import com.scc363.hospitalproject.datamodels.UserTypes;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.LoginAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HospitalControler {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    private LoginAuthService loginAuthService;


    @GetMapping("/add")
    public String addUserForm(User user) {
        return "add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute @Valid User u, Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "add";
        } else {
            userRepository.save(u);
            return "redirect:";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @GetMapping("/listusers")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "listusers";
    }

    @GetMapping("/getuser{id}")
    public String getUserById(@RequestParam int id) {
        return userRepository.findUserById(id).toString();
    }
}
