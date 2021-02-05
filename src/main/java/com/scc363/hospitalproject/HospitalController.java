package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;

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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    /*
    @Autowired
    private RegistrationService regService;
*/
    private final SessionManager sessionManager = new SessionManager();

    @GetMapping("/test")
    public String test() {
        return "test";

    /*
    @GetMapping("/signin")
    public String login(WebRequest request, Model model) {
        UserDTO u = new UserDTO();
        model.addAttribute("user", u);
        return "signin";
    }

    @GetMapping("/signin")
    public String login(WebRequest request, Model model) {
        UserDTO u = new UserDTO();
        model.addAttribute("user", u);
        return "signin";
    }


     */
    /**
     * Example login method to check, first of all if a user exists and if they have provided the correct password, secondly to
     * create a new session having destroy any existing ones using the ifUserHasSessionDestroy() method, then returning the
     * session data to be stored client side.
     * @param data post data in JSON format
     * @param request automatically provided request header to extract client IP from
     * @return returns a result based on decisions made inside the class either login failed or session data in JSON format for the client to store.
     *
     * example of returned JSON object -
     * {
     *     "sessionID"  : "h434gh34bjg4yrf78dee78",
     *     "privateKey" : "h434gh34bjg4yrf78dee78h434gh34bjg4+rf78dee78h434gh34bjg4yrf78dee78h434gh34bjg4yrf78de/\+78h434gh34bjg4yrf78dee78h434gh34bjg4yrf78dee78h434gh34bjg4yrf78dee78h434gh34bjg4yrf78dee78",
     *     "username"   : "john123"
     * }
     */
    @PostMapping("/signin")
    public String login(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        JSONObject dataObj = (JSONObject) dataArr.get(0);
        String userName = (String) dataObj.get("userName");
        String password = (String) dataObj.get("password");
        System.out.println(userName + password);
        /*
         Example conditional. Should be replaced to login method to check user exists and has provided correct password.
         1. Extract salt from DB.
         2. Append it to provided password plaintext.
         3. Hash new plaintext.
         4. Compare digest with DB stored version
         5. If equal, success, if not failure.
         */
        if (userRepository.findUserByUsername(userName) != null)
        {
            sessionManager.ifUserHasSessionDestroy(userName);
            JSONObject sessionData = sessionManager.createSession(userName, request.getRemoteAddr());
            if (sessionData != null)
            {
                return sessionData.toString();
            }
            else
            {
                return new JSONManager(new Pair[]{
                        new Pair("result", "incorrect login details")
                }).generateJSONObject().toString();
            }
        }
        else
        {
            return "error[no data provided]";
        }
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
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        try {
            User registered = regService.registerNewUser(DTOMapper.userDtoToEntity(userDTO));
            registered.sendEmail(userDTO.getEmail());
            System.out.println("===========\n User added");
        } catch (UserAlreadyExistsException e) {
            ModelAndView model = new ModelAndView();
            System.out.println("===========\n Adding User failed");
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

//    @GetMapping("/verify-account")
//    public String showVerifyAccount(WebRequest request, Model model) {
////        ModelAndView mav = new ModelAndView();
////        mav.setViewName("verify-account");
////        mav.addObject("verification", new VerificationDTO());
//        model.addAttribute("verification", new VerificationDTO());
////        return mav;
//        return "verify-account";
//    }
//
//    @PostMapping("/verify-account")
//    public ModelAndView processVerifyAccount(@ModelAttribute("verification") @Valid VerificationDTO verification, BindingResult result, HttpServletRequest request, Errors errors) {
////        JSONArray data = new JSONManager().convertToJSONObject(ver);
////        System.out.println(data);
////        JSONObject dataObj = (JSONObject) data.get(0);
////        String email = (String) dataObj.get("email");
////        String code = (String) dataObj.get("code");
//        ModelAndView mav = new ModelAndView();
////        System.out.println("==========\nVerified user");
////        System.out.println(email);
////        System.out.println(code);
//        String email = verification.email();
//        String code = verification.code();
//
//        if (result.hasErrors()) {
//            System.out.println(result.getAllErrors());
//
//            mav.setViewName("verify-account");
//
//            return mav;
//        }
//
//        if (userRepository.existsByEmailAndCode(email, Integer.parseInt(code))) {
//            System.out.println("==========\nVerified user");
//            // User authenticated
//            User u = userRepository.findUserByEmail(email);
//            u.enableAccount();
//            userRepository.save(u);
//            mav.setViewName("verify-account");
//
//            mav.addObject("success");
//            return mav;
//        } else {
//            mav.setViewName("verify-account");
//            mav.addObject("failure");
//            return mav;
//        }
//    }


    // Test mapping
    @GetMapping("/hello")
    public String hello() {
        return "hello";
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


    /*
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
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        try {
            User registered = regService.registerNewUser(DTOMapper.userDtoToEntity(userDTO));
            registered.sendEmail(userDTO.getEmail());
            System.out.println("===========\n User added");
        } catch (UserAlreadyExistsException e) {
            ModelAndView model = new ModelAndView();
            System.out.println("===========\n Adding User failed");
            model.addObject("userExistsError", "An account for that username/email already exists.");
            model.addObject("userDTO", userDTO);
            model.setViewName("register");
            return model;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("verify-account");
        mav.addObject("emailAddress", userDTO.getEmail());
        return mav;
    }

    @GetMapping("/verify-account")
    public ModelAndView showVerifyAccount(WebRequest request, Model model, UserDTO userDTO) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("verify-account");
        mav.addObject("user", userDTO);
        return mav;
    }

    @PostMapping("/verify-account")
    public ModelAndView processVerifyAccount(@RequestBody VerificationDTO ver, BindingResult result, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        System.out.println("==========\nVerified user");

        if (userRepository.existsByEmailAndCode(ver.email(), Integer.parseInt(ver.code()))) {
            System.out.println("==========\nVerified user");
            // User authenticated
            User u = userRepository.findUserByEmail(ver.email());
            u.enableAccount();
            userRepository.save(u);
            mav.setViewName("verify-account");

            mav.addObject("success");
            return mav;
        } else {
            mav.setViewName("verify-account");
            mav.addObject("failure");
            return mav;
        }
    }


    // Test mapping
    @GetMapping("/hello")
    public String hello() {
        return "hello";
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



    /**
     * Example method to test if a given user and computer have an active and valid session. Takes in a JSON request object, obtains the client stored username, sessionID
     * and encrypted private key, passes this data to the SessionManager class for validation and then provides a result in JSON format using the JSONManager class.
     * @param data post data in JSON format
     * @param request automatically provided request header to extract client IP from
     * @return JSON string result of authenticatio.
     */
    @PostMapping("/isAuth")
    public String isAuthenticated(@RequestParam String data, HttpServletRequest request)
    {

        JSONArray dataArr = new JSONManager().convertToJSONObject(data); //converts JSON string into JSON object
        JSONObject sessionObject = (JSONObject) dataArr.get(0); //This is the position in the JSON array that the session credentials have been stored.
        return new JSONManager().getResponseObject(sessionManager.isAuthorised(sessionObject, request.getRemoteAddr()));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("=============\ntoads");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }






    @PostMapping("/controlPanel")
    public String provideControlPanel(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        JSONObject sessionObject = (JSONObject) dataArr.get(0);
        if (sessionManager.isAuthorised(sessionObject, request.getRemoteAddr()))
        {
            User user = userRepository.findUserByUsername((String) sessionObject.get("username"));
            return "control panel";
        }
        else
        {
            return "redirect:login";
        }
    }

    @GetMapping("/addPatient")
    public String getCreatePatient(Model model) {
        model.addAttribute("patient", new PatientDetails());
        return "addPatient";
    }


    @PostMapping("/createPatientService")
    public String createPatient(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        if (dataArr != null)
        {
            if (dataArr.size() > 0)
            {
                JSONObject sessionObject = (JSONObject) dataArr.get(0);
                if (sessionManager.isAuthorised(sessionObject, request.getRemoteAddr()))
                {
                    User user = userRepository.findUserByUsername((String) sessionObject.get("username"));
                    if (user.hasRole(roleRepository.findByName("REGULATOR")) || user.hasRole(roleRepository.findByName("MED_ADMIN")))
                    {
                        JSONObject dataObject = (JSONObject) dataArr.get(1);
                        PatientDetails newPatient = new PatientDetails();
                        newPatient.setFirstName((String) dataObject.get("firstName"));
                        newPatient.setLastName((String) dataObject.get("firstName"));
                        newPatient.setMedicalID((int) dataObject.get("medId"));
                        newPatient.setPhoneNumber((int) dataObject.get("phone"));
                        newPatient.setAddress((String) dataObject.get("firstName"));
                        newPatient.setWeight(Float.parseFloat((String) dataObject.get("firstName")));
                        newPatient.setHeight(Float.parseFloat((String) dataObject.get("firstName")));
                        newPatient.setDoctor((String) dataObject.get("firstName"));
                        patientDetailsRepository.save(newPatient);

                    }
                    else
                    {
                        return "access denied";
                    }
                }
            }
        }

        return "redirect:login";
    }


    @PostMapping("/getUsersService")
    public String listUsers(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        if (dataArr != null)
        {
            if (dataArr.size() > 0)
            {
                JSONObject sessionObject = (JSONObject) dataArr.get(0);
                if (sessionManager.isAuthorised(sessionObject, request.getRemoteAddr()))
                {
                    User user = userRepository.findUserByUsername((String) sessionObject.get("username"));
                    if (user.getRoles().get(0).hasPrivileges(privilegeRepository.findByName("READ_USERS")))
                    {
                        ArrayList<User> allUsers = new ArrayList<User>((Collection<? extends User>) userRepository.findAll());
                        JSONArray usersArray = new JSONArray();
                        for (User currentUser : allUsers)
                        {
                            JSONObject userObj = new JSONObject();
                            userObj.put("id", currentUser.getId());
                            userObj.put("username", currentUser.getUsername());
                            userObj.put("email", currentUser.getEmail());
                            userObj.put("userType", currentUser.getUserType());
                            usersArray.add(userObj);
                        }
                        return usersArray.toString();
                    }
                }
            }
        }

        return "redirect:login";
    }


    @GetMapping("/login")
    public String showLogin()
    {
        return "login.html";
    }



    @PostMapping("/testFo")
    @ResponseBody public String testFor(@RequestParam String data)
    {
        return String.format("%s", data);
    }


}
