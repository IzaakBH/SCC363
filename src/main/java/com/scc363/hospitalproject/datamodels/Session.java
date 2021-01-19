package com.scc363.hospitalproject.datamodels;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

public class Session
{
    /*

    login -> check user details -> generate RSA key pair -> encrypt IP with public key -> return crypto IP and public key -> browser stores key
    request -> sends public key and session key with request data -> checks session key exists -> if so encrypts remote IP with passed public key, compares to stored session key, decrypts encrypted session key with private key, compares with remote IP -> session authorised

     */


    private SecretKey key;
    private String sessionKey;
    private String salt;
    private long expiry;

    public Session(SecretKey key, String sessionKey, String salt)
    {
        this.key = key;
        this.sessionKey = sessionKey;
        this.expiry = System.currentTimeMillis() + 300000;
        this.salt = salt;
    }

    public boolean hasExpired()
    {
        return System.currentTimeMillis() > expiry;
    }

    public String getSessionKey()
    {
        return this.sessionKey;
    }

    public SecretKey getKey()
    {
        return this.key;
    }

    public String getSalt()
    {
        return this.salt;
    }
}
