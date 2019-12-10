package com.example.p5projectchat.Security;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class uses the Advanced encryption standard to handle security in the application
 */
public class AES {

    private String AES = "AES";

    private static final int ITERATION_COUNT = 1000;

    private static final int KEY_LENGTH = 256;

    private static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    private static final int PKCS5_SALT_LENGTH = 32;

    private static final String DELIMITER = "]";

    private static final SecureRandom random = new SecureRandom();


    /**
     * This method encrypts text
     * @param plaintext the Text to be encrypted
     * @param password The password to the encryption key
     * @return encrypted text
     */
    public static String encrypt(String plaintext, String password){
        byte[] salt = generateSalt();

        SecretKey key = deriveKey(password, salt);

        try{
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = generateIv(cipher.getBlockSize());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

            if (salt != null){
                return String.format("%s%s%s%s%s",
                        toBase64(salt),
                        DELIMITER,
                        toBase64(iv),
                        DELIMITER,
                        toBase64(cipherText));
            }

            return String.format("%s%s%s",
                    toBase64(iv),
                    DELIMITER,
                    toBase64(cipherText));
        } catch (GeneralSecurityException | UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }


    /**
     *
     * @param ciphertext Ecrypted text to decode
     * @param password The password to the encryption key
     * @return
     */
    public static String decrypt(String ciphertext, String password) {
        String[] fields = ciphertext.split(DELIMITER);
        if(fields.length != 3) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }
        byte[] salt        = fromBase64(fields[0]);
        byte[] iv          = fromBase64(fields[1]);
        byte[] cipherBytes = fromBase64(fields[2]);
        SecretKey key = deriveKey(password, salt);

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] plaintext = cipher.doFinal(cipherBytes);
            return new String(plaintext, "UTF-8");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initialization vector - to achieve semantic security, does not allow an attacker
     * to infer relationships between segments of the encrypted message.
     *
     */
    private static byte[] generateIv(int length) {
        byte[] b = new byte[length];
        random.nextBytes(b);
        return b;
    }

    /**
     * salt is random data and is used as an additional
     * input to a one-way function that hashes a password or passphrase
     */
    private static byte[] generateSalt() {
        byte[] b = new byte[PKCS5_SALT_LENGTH];
        random.nextBytes(b);
        return b;
    }

    /**
     * Is binary data in ASCII string format
     */
    private static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }

    /**
     *
     * This derives the key from the password
     */
    private static SecretKey deriveKey(String password, byte[] salt) {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF2_DERIVATION_ALGORITHM);
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptNumberTwo(String strToEncrypt, String password) {
        try {
            SecretKeySpec key = generateKeyNumbertwo(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encVal = cipher.doFinal(strToEncrypt.getBytes());
            String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
            return encryptedValue;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static String decryptNumberTwo(String strToDecrypt, String password) throws Exception{
        SecretKeySpec key = generateKeyNumbertwo(password);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.decode(strToDecrypt, Base64.DEFAULT);
        byte[] decVal = cipher.doFinal(decodedVal);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }

    private static SecretKeySpec generateKeyNumbertwo(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte [] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);

        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;

    }

}
