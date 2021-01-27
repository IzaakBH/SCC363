package com.scc363.hospitalproject.utils;

import com.scc363.hospitalproject.datamodels.KeyValue;
import com.scc363.hospitalproject.datamodels.Session;
import org.json.simple.JSONObject;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;

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


    public JSONObject createSession(String username, String clientIP)
    {
        CryptoManager cryptoManager = new CryptoManager();

        KeyPair keyPair = cryptoManager.generateKeyPair();
        SecretKey aesKey = cryptoManager.generateAESKey();
        String sessionID = cryptoManager.hashString((username + System.currentTimeMillis()));

        Session session = new Session(keyPair.getPublic(), sessionID, aesKey, username, cryptoManager.encrypt(clientIP, keyPair.getPublic()));

        if (sessionCache.createSession(session))
        {
            String privKCipher = cryptoManager.encryptAES(cryptoManager.keyToString(keyPair.getPrivate()), aesKey);
            return new JSONManager(
                    new KeyValue[]{
                            new KeyValue("sessionID", sessionID),
                            new KeyValue("privateKey", privKCipher),
                            new KeyValue("username", username)
                    }).generateJSONObject();
        }
        else
        {
            return null;
        }
    }


    public boolean isAuthorised(JSONObject dataObj, String clientIP)
    {
        String sessionID = (String) dataObj.get("sessionID");
        String privateKeyString = (String) dataObj.get("privateKey");
        String username = (String) dataObj.get("username");

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
        return false;
    }
}
