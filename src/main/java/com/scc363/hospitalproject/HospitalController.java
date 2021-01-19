package com.scc363.hospitalproject;


import com.scc363.hospitalproject.datamodels.*;
import com.scc363.hospitalproject.repositories.PatientDetailsRepository;
import com.scc363.hospitalproject.repositories.UserRepository;
import com.scc363.hospitalproject.services.CryptoManager;
import com.scc363.hospitalproject.services.JSONManager;
import com.scc363.hospitalproject.services.LoginAuthService;
import com.scc363.hospitalproject.services.SessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

@RestController
public class HospitalController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    private LoginAuthService loginAuthService;

    private SessionCache sessionCache = new SessionCache();


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



    @GetMapping("/auth")
    public String authenticate(@RequestParam String userName, @RequestParam String password, HttpServletRequest request)
    {
        if (userName.length() > 0 && password.length() > 0)
        {
            CryptoManager cryptoManager = new CryptoManager();
            String clientIP = request.getRemoteAddr();
            String salt = cryptoManager.generateSalt();
            SecretKey key = cryptoManager.generateAESKey();
            String sessionKeyPT = clientIP;
            byte[] sessionKeyCipher = cryptoManager.encryptAES(sessionKeyPT, key);
            String sessionKeyCT = Base64.getEncoder().encodeToString(sessionKeyCipher);
            Session session = new Session(key, sessionKeyCT, salt);

            if (sessionCache.createSession(session))
            {
                return new JSONManager(new KeyValue[]{new KeyValue("sessionKey", sessionKeyCT)}).generateJSONObject().toString();
            }
            else
            {
                return "error[unale to create session]";
            }
        }
        else
        {
            return "error[no data provided]";
        }
    }



    @GetMapping("/isauth")
    public String isAuthenticated(@RequestParam  String sessionKey, HttpServletRequest request)
    {
        sessionKey = sessionKey.replaceAll(" ", "+");
        if (sessionCache.hasSession(sessionKey))
        {
            Session session = sessionCache.getSession(sessionKey);
            if (!session.hasExpired())
            {
                CryptoManager cryptoManager = new CryptoManager();
                String remoteAddr = request.getRemoteAddr();
                String rebuiltSessionKey = session.getSessionKey();

                if (cryptoManager.decryptAES(sessionKey.getBytes(), session.getKey()).equals(rebuiltSessionKey))
                {
                    return "session authorised";
                }
                else
                {
                    return "session not authorised";
                }
            }
            else
            {
                sessionCache.destroySession(sessionKey);
                return "error[session has expired]";
            }
        }
        else
        {
            System.out.println(sessionKey + " does not exist inncahce");
            return "error[invalid session code]";
        }
    }

    @GetMapping("/listsessions")
    public String listSessions()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Session session : sessionCache.sessions)
        {
            stringBuilder.append(session.getSessionKey() + "<br>");
        }

        return stringBuilder.toString();
    }
}
