package com.scc363.hospitalproject;
import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;
import com.scc363.hospitalproject.exceptions.PatientAlreadyExistsException;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;

import com.scc363.hospitalproject.repositories.*;
import com.scc363.hospitalproject.services.*;
import com.scc363.hospitalproject.utils.DTOMapper;
import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.Pair;

import com.scc363.hospitalproject.utils.SessionManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import javax.swing.*;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

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

    @Autowired
    private UserManager userManager;
    @Autowired
    private PermittedUserRepository permittedUserRepository;

    private final SessionManager sessionManager = new SessionManager();

    private boolean checkDB = false;

    @GetMapping("/login")
    public String login() {
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
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
        if (loginService.isAuthenticated(userName, password))
        {
            sessionManager.ifUserHasSessionDestroy(userName);
            ArrayList<Cookie> cookies = sessionManager.createSession(userName, request.getRemoteAddr());
            if (cookies != null)
            {
                for (Cookie cookie : cookies)
                {
                    response.addCookie(cookie);
                }
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "User logged in", userName));
                return "success";
            }
            else
            {
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "warn", "Error loggin in", userName));
                int users= logsRepository.countByLevelAndUserNameAndDate("warn", userName, LocalDate.now());
                System.out.println(users);
                if(users>=5){
                    Email warnEmail = new Email();
                    warnEmail.sendEmail(userRepository.findUserByUsername(userName).getEmail(), "Someone tried to login to your account for 3 times with the wrong credentials");
                }

                int warns = logsRepository.countByLevel("warn");
                if(warns>=500){
                    Email warnEmail = new Email();
                    warnEmail.sendEmail("scc363gr@gmail.com", "Many warns available");
                }
                return "failure";
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "warn", "Error logging in", userName));
        int users= logsRepository.countByLevelAndUserNameAndDate("warn", userName, LocalDate.now());
        System.out.println(users);
        if(users>=5){
            Email warnEmail = new Email();
            warnEmail.sendEmail(userRepository.findUserByUsername(userName).getEmail(), "Someone tried to login to your account for 3 times with the wrong credentials");
        }

        int warns = logsRepository.countByLevel("warn");
        if(warns>=500){
            Email warnEmail = new Email();
            warnEmail.sendEmail("scc363gr@gmail.com", "Many warns available");
        }
        return "failure";
    }

    @GetMapping("/home")
    public String isAuthenticated(HttpServletRequest request) {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Home page loaded", null));
                return "home";
            }

        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "debug", "Sign in page loaded", null));
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
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());

            ModelAndView model = new ModelAndView();
            System.out.println("===========\n Adding User failed");
            logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Adding user failed", null));
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        ModelAndView mav = new ModelAndView();

        if (permittedUserRepository.findPermittedUserByEmailAndUserType(userDTO.getEmail(), userDTO.getUserType()) != null)
        {
            try {
                User registered = regService.registerNewUser(DTOMapper.userDtoToEntity(userDTO));
                registered.sendEmail(userDTO.getEmail());
                System.out.println("===========\n User added");
                System.out.println("user type " + registered.getUserType());
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "User is registered", null));
            } catch (UserAlreadyExistsException e) {
                ModelAndView model = new ModelAndView();
                System.out.println("===========\n Adding User failed");
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User registration failed", null));
                model.addObject("userExistsError", "An account for that username/email already exists.");
                model.addObject("userDTO", userDTO);
                model.setViewName("register");
                return model;
            }

            mav.setViewName("verifymessage");
            return mav;
        }
        else
        {
            System.out.println("could not find " + userDTO.getEmail() + " and " + userDTO.getUserType() + " in permitted users");
        }
        mav.setViewName("error2");
        return mav;
    }

    @GetMapping("/verifymessage")
    public String showVerifyMessage() {
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "debig", "Verification message sent", null));
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
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User not verified", u.getUsername()));
                    return "notverified";
                } else if (u.getCode().equals(token)) {
                    u.setEnabled(true);
                    userRepository.save(u);
                    model.addAttribute("success", true);
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "debug", "User verified", u.getUsername()));
                    return "verified";
                }
            } else {
                model.addAttribute("failure", false);
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User not verified", null));
                return "notverified";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("failure", false);
            logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User not verified", null));
            return "notverified";
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User not verified", null));
        return "notverified";
    }


    @GetMapping("/delete-account/{id}")
    public String accountsEdit(@PathVariable Integer id, Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("DELETE_USERS")))
                    {
                        try {
                            userRepository.deleteUserById(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Account could not be deleted", userRepository.findUserById(id).getUsername()));
                        }
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "Account deleted", userRepository.findUserById(id).getUsername()));
                        return "delete-account";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Account could not be deleted", userRepository.findUserById(id).getUsername()));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }


    @GetMapping("/accounts")
    public String getUsers(Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_USERS")))
                    {
                        model.addAttribute("users", userRepository.findAll());
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "Account list loaded", null));
                        return "accounts";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "Account list could not be loaded", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @GetMapping("/getuser{id}")
    public String getUserById(@RequestParam int id, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_USERS")))
                    {
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "User retrieved", user.getUsername()));
                        return userRepository.findUserById(id).toString();
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Error while retrieving user", user.getUsername()));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
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

                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("WRITE_PATIENTS")))
                    {
                        model.addAttribute("patient", new PatientDetails());
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "New patient added", null));
                        return "addPatient";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Could not add new patient", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
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
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
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

    @GetMapping("/records")
    public String records(Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_PATIENTS")))
                    {
                        model.addAttribute("patients", patientDetailsRepository.findAll());
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "Patients records retireved", null));
                        return "records";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Error while loading patients records", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @GetMapping("/viewPatient/{id}")
    public String viewPatient(@PathVariable String id, Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_PATIENTS")) || user.hasPrivilege(privilegeRepository.findByName("READ_ALL_PATIENTS")))
                    {
                        PatientDetails patientDetails = patientDetailsRepository.getPatientDetailsByMedicalID(id);
                        if (patientDetails.getDoctor().equals(user.getUsername()))
                        {
                            try {
                                PatientDetails patient = patientDetailsRepository.getPatientDetailsByMedicalID(id);
                                model.addAttribute("patient", patient);
                            } catch (Exception e) {
                                e.printStackTrace();
                                PatientDetails patient = new PatientDetails();
                            }
                            logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "Patients data viewed", patientDetails.getFirstName()+ patientDetails.getLastName()));
                            return "viewPatient";
                        }
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Patients data loading error", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";

    }


    @GetMapping("/addPermittedUser")
    public String showAddPermittedUser(HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null) {
                    if (user.hasRole(roleRepository.findRoleByName("SYSTEM_ADMIN")))
                    {
                        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Permitted user showed", user.getUsername()));
                        return "addpermitteduser";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Could not show permitted user", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @PostMapping("/preAuthUser")
    public String preAuthUser(@RequestParam String email, @RequestParam String userType, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasRole(roleRepository.findRoleByName("SYSTEM_ADMIN")))
                    {
                        if (userRepository.findUserByEmail(email) == null)
                        {
                            if (email.contains("@") && email.contains("."))
                            {
                                if (roleRepository.findRoleByName(userType) != null)
                                {
                                    PermittedUser permittedUser = new PermittedUser(email, userType);
                                    permittedUserRepository.save(permittedUser);
                                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "User is preauthenticated", user.getUsername()));
                                    return "controlpanel";
                                }
                                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User could not be preauthenticated", null));
                                return "error3";
                            }
                        }
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "User could not be preauthenticated", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @GetMapping("/accountsEdit/{username}")
    public String accountsEdit(@PathVariable String username, Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_USERS")))
                    {
                            try {
                                User targetuser = userRepository.findUserByUsername(username);
                                model.addAttribute("user", targetuser);
                                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Account is edited", username));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return "accountsEdit";
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Could not edit account", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @GetMapping("/logs")
    public String logs(Model model, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null)
                {
                    if (user.hasPrivilege(privilegeRepository.findByName("READ_LOGS")))
                    {
                        model.addAttribute("logs", logsRepository.findAll());
                        return "logs";
                    }
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }





    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Integer id, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null) {
                    if (user.hasPrivilege(privilegeRepository.findByName("DELETE_USERS")))
                    {
                        if (userRepository.findUserById(id) != null)
                        {
                            userRepository.delete(userRepository.findUserById(id));
                            logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "info", "User deleted"+ userRepository.findUserById(id).getUsername(), userRepository.findUserById(id).getUsername()));
                            return "success";
                        }
                    }
                    logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Could not delete user", null));
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }


    @GetMapping("/deletePatient")
    public String deletePatient(@RequestParam String medicalID, HttpServletRequest request)
    {
        if (request.getCookies().length == 3)
        {
            if (sessionManager.isAuthorised(request.getCookies(), request.getRemoteAddr()))
            {
                User user = userManager.findUserByUsername(sessionManager.getCookie("username", request.getCookies()));
                if (user != null) {
                    if (user.hasPrivilege(privilegeRepository.findByName("DELETE_PATIENTS")))
                    {
                        if (patientDetailsRepository.getPatientDetailsByMedicalID(medicalID) != null)
                        {
                            patientDetailsRepository.delete(patientDetailsRepository.getPatientDetailsByMedicalID(medicalID));
                            return "success";
                        }
                    }
                    return "error2";
                }
            }
        }
        logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "trace", "Sign in page loaded", null));
        return "signin";
    }

    @GetMapping("/logslist")
    public String getLogs(Model model) {
        model.addAttribute("logs", logsRepository.findAll());
        return "logslist";
    }

    /* Backup database */

    private void backupDB() {

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = localDate.getDayOfMonth();

        if (day == 1 && checkDB == false) {
            try {
                Class.forName("org.h2.Driver");
                Connection con = DriverManager.getConnection("jdbc:h2:" + "./data/userdata", "sa", "password");
                Statement stmt = con.createStatement();
                con.prepareStatement("BACKUP TO 'backup.zip'").executeUpdate();
                checkDB = true;
                System.out.println("hola");
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "debug", "Database backup created", null));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
                logsRepository.save( new Log(LocalDate.now(), LocalTime.now(), "error", "Problem backing up the db", null));
            }
        }
        if (day == 2){
            checkDB= false;
        }
    }

}
