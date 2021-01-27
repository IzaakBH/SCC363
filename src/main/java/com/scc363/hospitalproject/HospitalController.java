package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.RoleRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.*;
import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.SessionManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HospitalController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    private LoginAuthService loginAuthService;

    private final SessionManager sessionManager = new SessionManager();


    /*
    @PostMapping("/add")
    public String addUser(@RequestParam String userName, @RequestParam String password, @RequestParam String userType){
        User u = new User();
        u.setUsername(userName);
        u.setPassword(password);
        //TODO: Validation for non proper type here if a manual request is made.
        u.setUserType(String.valueOf(userType));
        userRepository.save(u);
        return String.format("Added %s to the database!", userName);
    }

    @GetMapping("/listusers")
    public Iterable<User> getUsers()
    {
        return userRepository.findAll();
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
    @PostMapping("/login")
    public String login(@RequestParam String data, HttpServletRequest request)
    {
        JSONArray dataArr = new JSONManager().convertToJSONObject(data);
        JSONObject dataObj = (JSONObject) dataArr.get(0);
        String userName = (String) dataObj.get("userName");
        String password = (String) dataObj.get("password");
        /*
         Example conditional. Should be replaced to login method to check user exists and has provided correct password.
         1. Extract salt from DB.
         2. Append it to provided password plaintext.
         3. Hash new plaintext.
         4. Compare digest with DB stored version
         5. If equal, success, if not failure.
         */
        if (userName.length() > 0 && password.length() > 0 /* loginMethod(username, password) */)
        {
            sessionManager.ifUserHasSessionDestroy(userName);
            JSONObject sessionData = sessionManager.createSession(userName, request.getRemoteAddr());
            if (sessionData != null)
            {
                return sessionData.toString();
            }
            else
            {
                return new JSONManager(new KeyValue[]{
                        new KeyValue("result", "incorrect login details")
                }).generateJSONObject().toString();
            }
        }
        else
        {
            return "error[no data provided]";
        }
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

    @GetMapping("/testpriv")
    public String testPriv(@RequestParam String username)
    {
        User user = userRepository.findUserByUsername(username);
        if (user != null)
        {
            if (user.hasRole(roleRepository.findByName("DOCTOR")))
            {
                return "all good";
            }
        }
        return "false";
    }

}
