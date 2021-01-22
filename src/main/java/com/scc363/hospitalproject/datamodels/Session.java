package com.scc363.hospitalproject.datamodels;

import com.scc363.hospitalproject.services.CryptoManager;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Session
{


    private PublicKey publicKey;
    private SecretKey aesKey;
    private String sessionID;
    private long expiry;
    private String user;
    private byte[] challenge;

    public Session(PublicKey key, String sessionID, SecretKey aesKey, String user, byte[] challenge)
    {
        this.publicKey = key;
        this.sessionID = sessionID;
        this.expiry = System.currentTimeMillis() + 300000;
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

    /*
    public PublicKey getKey()
    {
        return this.publicKey;
    }

     */


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