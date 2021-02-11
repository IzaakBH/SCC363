package com.scc363.hospitalproject.datamodels;

import com.scc363.hospitalproject.utils.CryptoManager;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Session
{


    private SecretKey aesKey;
    private String sessionID;
    private long expiry;
    private String user;
    private byte[] challenge;


    public Session(String sessionID, SecretKey aesKey, String user, byte[] challenge)
    {
        this.sessionID = sessionID;
        this.expiry = System.currentTimeMillis() + 18000000;
        this.aesKey = aesKey;
        this.user = user;
        this.challenge = challenge;
    }

    public SecretKey getAesKey()
    {
        return this.aesKey;
    }

    public String getUser()
    {
        return this.user;
    }

    public boolean hasExpired()
    {
        return System.currentTimeMillis() > expiry;
    }

    public String getSessionID()
    {
        return this.sessionID;
    }


    public boolean isAuthenticated(PrivateKey privateKey, String data)
    {
        CryptoManager cryptoManager = new CryptoManager();
        if (cryptoManager.decrypt(challenge, privateKey).equals(data))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
