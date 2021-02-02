package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.datamodels.dtos.UserDTO;
import com.scc363.hospitalproject.exceptions.UserAlreadyExistsException;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.*;
import com.scc363.hospitalproject.utils.DTOMapper;
import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HospitalController
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientDetailsRepository patientDetailsRepository;
    @Autowired
    private RegistrationService regService;

    @Autowired
    private LoginService loginService;

    private final SessionManager sessionManager = new SessionManager();

    @GetMapping("/signin")
    public String login()
    {
        return "signin";
    }


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
    @PostMapping("/createSession")
    @ResponseBody
    public String login(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        JSONObject dataObj = (JSONObject) dataArr.get(0);
        String userName = (String) dataObj.get("username");
        String password = (String) dataObj.get("password");
        System.out.println(userName + password);
        if (loginService.isAuthenticated(userName, password))
        {
            sessionManager.ifUserHasSessionDestroy(userName);
            JSONObject sessionData = sessionManager.createSession(userName, request.getRemoteAddr());
            if (sessionData != null)
            {
                System.out.println("========= created session ---------");
                return sessionData.toString();
            }
        }
        return new JSONManager().getResponseObject(false);
    }

    @RequestMapping(value="/testHome", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String testAll()
    {
        return "test home";
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



    /**
     * Example method to test if a given user and computer have an active and valid session. Takes in a JSON request object, obtains the client stored username, sessionID
     * and encrypted private key, passes this data to the SessionManager class for validation and then provides a result in JSON format using the JSONManager class.
     * @param data post data in JSON format
     * @param request automatically provided request header to extract client IP from
     * @return JSON string result of authenticatio.
     */
    @PostMapping("/controlPanel")
    public String isAuthenticated(@RequestParam(required = false) String data, HttpServletRequest request)
    {

        if (data != null)
        {
            JSONArray dataArr = new JSONManager().convertToJSONObject(data); //converts JSON string into JSON object
            JSONObject sessionObject = (JSONObject) dataArr.get(0); //This is the position in the JSON array that the session credentials have been stored.
            if (sessionManager.isAuthorised(sessionObject, request.getRemoteAddr()))
            {
                return "hello";
            }
        }
        return "signin";
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


}
