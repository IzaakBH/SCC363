package com.scc363.hospitalproject.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class CryptoManager
{


    /**
     * This method takes in some string, uses the SHA-256 algorithm and hashes it and then returns the hash digest as a String
     * @param data data to hash
     * @return hash digest
     */
    public String hashString(String data)
    {
        try
        {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = hash.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * this method takes in an integer between 0 and 127 and converts it to its ASCII char.
     * @param intVal integer value of the char
     * @return String value of the ASCII char
     */
    private String getChar(int intVal)
    {
        StringBuilder ascii = new StringBuilder();
        if (intVal >= 0 && intVal <= 127) //asserts integer is within the ASCII bounds
        {
            int charLen = 4; //size of an integer
            for (int i = charLen - 1; i >= 0; i--)
            {
                ascii.append((char) ((intVal >> (8 * i) & 0xFF))); //bit shift right and bitwise and with 0xFF hex
            }
        }
        return ascii.toString();
    }


    /**
     * This method generates a 128 bit salt value for
     * @return salt string
     */
    public String generateSalt()
    {
        StringBuilder salt = new StringBuilder();
        //loop generates 128 randomly generated characters from ASCII
        for(int i = 0; i < 16; i++)
        {
            //Secure random is considered to be 'Crypto graphically secure'.
            SecureRandom secureRandom = new SecureRandom();
            salt.append(getChar(secureRandom.nextInt(126-33+1)+33));
        }
        return salt.toString();
    }

    /**
     * generates an RSA key pair with a size of 2048
     * @return KeyPair object with private and public keys
     */
    public KeyPair generateKeyPair()
    {
        try
        {
            SecureRandom secureRandom = new SecureRandom();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        }
        catch (Exception e)
        {
            System.out.println("Unable to generate key pair");
        }
        return null;
    }


    /**
     *encrypts some String data with a public key
     * @param data data to encrypt
     * @param key key to encrypt with
     * @return byte array of encrypted data or null if cryptographic process fail
     */
    public byte[] encrypt(String data, PublicKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes());
        }
        catch (Exception e)
        {
            System.out.println("Unable to encrypt data");
        }

        return null;
    }

    /**
     * descrpts some encrypted data in the form of a byte array back to a String
     * @param cipherText byte array cipher text
     * @param key private key to decrypt with
     * @return plain text result of type String or null if cryptographic process fail
     */
    public String decrypt(byte[] cipherText, PrivateKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextArr = cipher.doFinal(cipherText);
            return new String(plainTextArr);
        }
        catch (Exception e)
        {
            System.out.println("Unable to decrypt data");
        }

        return null;
    }

    public String encryptAES(String data, SecretKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Unable to encrypt data");
        }

        return null;
    }

    public String decryptAES(String cipherText, SecretKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        }
        catch (Exception e)
        {
            System.out.println("Unable to decrypt data");
            e.printStackTrace();
        }

        return null;
    }



    /**
     * converts a key to a string key can be either public or private type
     * @param key Key instance
     * @return String type of key or null if class type is incorrect
     */
    public String keyToString(Key key)
    {
        if (key instanceof PublicKey)
        {
            PublicKey publicKey = (PublicKey) key;
            byte[] keyArr = publicKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyArr);
        }
        else if (key instanceof PrivateKey)
        {
            PrivateKey publicKey = (PrivateKey) key;
            byte[] keyArr = publicKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyArr);
        }
        return null;
    }


    /**
     * this converts a public key from a string back into an RSA public key
     * @param key key to convert
     * @return PublicKey object or null
     */
    public PublicKey getPublicKeyFromString(String key)
    {
        try
        {
            byte[] publicKey = Base64.getDecoder().decode(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to get public key");
        }
        return null;
    }

    /**
     * converts String back into RSA private key
     * @param key key to convert
     * @return PrivateKey Object or null
     */
    public PrivateKey getPrivateKeyFromString(String key)
    {

        try
        {
            byte[] publicKey = Base64.getDecoder().decode(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (PrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(publicKey));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to get private key");
        }
        return null;

    }


    public String generateSessionCode(String IP)
    {
        return hashString(IP + generateSalt());
    }

    public SecretKey generateAESKey()
    {
        try
        {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, secureRandom);
            return keyGenerator.generateKey();
        }
        catch (Exception e)
        {
            System.out.println("Unable to generate AES Key");
            e.printStackTrace();
        }

        return null;
    }

}
