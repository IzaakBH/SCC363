package com.scc363.hospitalproject.utils;

import com.scc363.hospitalproject.utils.CryptoManager;
import com.scc363.hospitalproject.utils.JSONManager;
import com.scc363.hospitalproject.utils.Pair;
import com.scc363.hospitalproject.datamodels.Session;
import org.json.simple.JSONObject;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;

public class SessionManager
{
    private final SessionCache sessionCache;

    public SessionManager()
    {
        this.sessionCache = new SessionCache();
    }


    public void ifUserHasSessionDestroy(String username)
    {
        Session userSession = sessionCache.getSessionByUser(username);
        if (userSession != null)
        {
            sessionCache.destroySession(userSession.getSessionID(), username);
        }
    }



    public ArrayList<Cookie> createSession(String username, String clientIP)
    {
        CryptoManager cryptoManager = new CryptoManager();

        KeyPair keyPair = cryptoManager.generateKeyPair();
        SecretKey aesKey = cryptoManager.generateAESKey();
        String sessionID = cryptoManager.hashString((username + new CryptoManager().generateSalt()));

        Session session = new Session(sessionID, aesKey, username, cryptoManager.encrypt(clientIP, keyPair.getPublic()));

        if (sessionCache.createSession(session))
        {
            String privKCipher = cryptoManager.encryptAES(cryptoManager.keyToString(keyPair.getPrivate()), aesKey);
            Cookie sessionIDCookie = new Cookie("sessionID", sessionID);
            Cookie privateKeyCookie = new Cookie("key", privKCipher);
            Cookie usernameCookie = new Cookie("username", username);

            usernameCookie.isHttpOnly();
            privateKeyCookie.isHttpOnly();
            usernameCookie.isHttpOnly();

            //max age is 30 minutes
            usernameCookie.setMaxAge(18000000);
            privateKeyCookie.setMaxAge(18000000);
            privateKeyCookie.setMaxAge(18000000);
            return new ArrayList<>(Arrays.asList(sessionIDCookie, privateKeyCookie, usernameCookie));

        }
        else
        {
            return null;
        }
    }




    public boolean isAuthorised(Cookie[] cookies, String clientIP)
    {
        String sessionID = getCookie("sessionID", cookies);
        String privateKeyString = getCookie("key", cookies);
        String username = getCookie("username", cookies);

        if (sessionID != null && privateKeyString != null && username != null)
        {
            privateKeyString = privateKeyString.replaceAll(" ", "+");
            sessionID = sessionID.replaceAll(" ", "+");

            if (sessionCache.hasSession(sessionID, username))
            {
                Session session = sessionCache.getSession(sessionID, username);
                if (!session.hasExpired())
                {
                    CryptoManager cryptoManager = new CryptoManager();

                    String privKeyPT = cryptoManager.decryptAES(privateKeyString, session.getAesKey());
                    if (privKeyPT != null)
                    {
                        PrivateKey privateKey = cryptoManager.getPrivateKeyFromString(privKeyPT);
                        if (privateKey != null)
                        {
                            return session.isAuthenticated(privateKey, clientIP);
                        }
                    }
                }
                else
                {
                    sessionCache.destroySession(sessionID, username);
                }
            }
        }

        return false;
    }


    public String getCookie(String name, Cookie[] cookies)
    {
        for (Cookie cookie : cookies)
        {
            if (cookie.getName().equals(name))
            {
                return cookie.getValue();
            }
        }
        return null;
    }
}
