package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;
import com.scc363.hospitalproject.exceptions.PatientAlreadyExistsException;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;

import com.scc363.hospitalproject.repositories.LogsRepository;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.PrivilegeRepository;
import com.scc363.hospitalproject.repositories.RoleRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.*;
import com.scc363.hospitalproject.utils.DTOMapper;
import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.Pair;

import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.SessionManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HospitalController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientDetailsRepository patientDetailsRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RegistrationService regService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private LoginService loginService;

    @Autowired
    private LogsRepository logsRepository;

    private final SessionManager sessionManager = new SessionManager();

    @GetMapping("/login")
    public String login() {
        return "signin";
    }

    @PostMapping("/createSession")
    @ResponseBody
    public String login(@RequestParam String data, HttpServletRequest request, HttpServletResponse response)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        JSONObject dataObj = (JSONObject) dataArr.get(0);
        String userName = (String) dataObj.get("username");
        String password = (String) dataObj.get("password");
        System.out.println(userName + password);
        if (/*loginService.isAuthenticated(userName, password)*/true)
        {
            sessionManager.ifUserHasSessionDestroy(userName);
            ArrayList<Cookie> cookies = sessionManager.createSession(userName, request.getRemoteAddr());
            if (cookies != null)
            {
                for (Cookie cookie : cookies)
                {
                    response.addCookie(cookie);
                }
                return "success";
            }
            else
            {
                return "failure";
            }
        }
        return "failure";
    }

    @GetMapping("/controlPanel")
    public String isAuthenticated(HttpServletRequest request) {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                return "hello";
            }
        }
        return "signin";
    }

    @GetMapping("/register")
    public String showRegistration(WebRequest request, Model model) {

        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView processRegistration(@ModelAttribute @Valid UserDTO userDTO, BindingResult result, HttpServletRequest request, Errors errors) {

        logsRepository.save(new Log(LocalDateTime.now(), "info", "registration", null));
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());

            ModelAndView model = new ModelAndView();
            System.out.println("===========\n Adding User failed");
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        try {
            User registered = regService.registerNewUser(DTOMapper.userDtoToEntity(userDTO));
            registered.sendEmail(userDTO.getEmail());
            System.out.println("===========\n User added");
            logsRepository.save(new Log(LocalDateTime.now(), "info", "user added" + registered.getUsername() , null));
        } catch (UserAlreadyExistsException e) {
            ModelAndView model = new ModelAndView();
            System.out.println("===========\n Adding User failed");
            logsRepository.save(new Log(LocalDateTime.now(), "error", "registration failed", null));
            model.addObject("userExistsError", "An account for that username/email already exists.");
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("verifymessage");
        return mav;
    }

    @GetMapping("/verifymessage")
    public String showVerifyMessage() {
        return "verifymessage";
    }

    @GetMapping("/verify-account")
    public String processVerification(Model model, @RequestParam("email") String email ,@RequestParam("token") String token) {
        System.out.println(token);
        System.out.println(email);

        try {
            if (userRepository.existsByEmail(email)) {
                System.out.println("==========\nVerified user");
                // User authenticated
                User u = userRepository.findUserByEmail(email);

                if (u.isEnabled()) {
                    model.addAttribute("failure", false);
                    return "notverified";
                } else if (u.getCode().equals(token)) {
                    u.setEnabled(true);
                    userRepository.save(u);
                    model.addAttribute("success", true);
                    return "verified";
                }
            } else {
                model.addAttribute("failure", false);
                return "notverified";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("failure", false);
            return "notverified";
        }
        return "notverified";
    }


    @GetMapping("/listusers")
    public String getUsers(Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_USERS")))
                    {
                        model.addAttribute("users", userRepository.findAll());
                        return "listusers";
                    }
                    return "error2";
                }
            }
        }
        return "signin";
    }

    @GetMapping("/getuser{id}")
    public String getUserById(@RequestParam int id, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_USERS")))
                    {
                        return userRepository.findUserById(id).toString();
                    }
                    return "error2";
                }
            }
        }
        return "signin";
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        System.out.println("=============\ntoads");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @GetMapping("/addPatient")
    public String getCreatePatient(Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {

                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("WRITE_PATIENTS")))
                    {
                        model.addAttribute("patient", new PatientDetails());
                        return "addPatient";
                    }
                    return "error2";
                }
            }
        }
        return "signin";
    }

    @PostMapping("/addPatient")
    public ModelAndView addPatient(@ModelAttribute @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Valid PatientDetails patientDetails, BindingResult result, HttpServletRequest request, Errors errors)
    {
        ModelAndView mav = new ModelAndView();

        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("WRITE_PATIENTS")))
                    {
                        if (result.hasErrors()) {
                            System.out.println(result.getAllErrors());

                            ModelAndView model = new ModelAndView();
                            System.out.println("===========\n Adding Patient failed");
                            model.addObject("patient", patientDetails);
                            model.setViewName("addPatient");
                            return model;
                        }

                        //TODO: Add user validation to make sure they can add patients

                        try {
                            PatientDetails registered = patientService.registerPatient(patientDetails);
                            System.out.println("===========\n Patient added");
                        } catch (PatientAlreadyExistsException e) {
                            ModelAndView model = new ModelAndView();
                            System.out.println("===========\n Adding Patient failed");
                            model.addObject("patientExistsError", "An patient already exists with this medial ID.");
                            model.addObject("patient", patientDetails);
                            model.setViewName("addPatient");
                            return model;
                        }

                        mav.setViewName("listPatients");
                        return mav;
                    }
                    mav.setViewName("error2");
                }
            }
        }
        mav.setViewName("sigin");
        return mav;
    }

    @GetMapping("/listPatients")
    public String listPatients(HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_PATIENTS")))
                    {
                        return "listPatients";
                    }
                    return "error2";
                }
            }
        }
        return "signin";
    }

    @GetMapping("/viewPatient/{id}")
    public String viewPatient(@PathVariable String id, Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userRepository.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_PATIENTS")))
                    {
                        if (patientDetailsRepository.hasDoctor(user.getUsername(), id))
                        {
                            try {
                                PatientDetails patient = patientDetailsRepository.getPatientDetailsByMedicalID(id);
                                model.addAttribute("patient", patient);
                            } catch (Exception e) {
                                e.printStackTrace();
                                PatientDetails patient = new PatientDetails();
                            }
                            return "viewPatient";
                        }
                    }
                    return "error2";
                }
            }
        }
        return "signin";

    }







}
