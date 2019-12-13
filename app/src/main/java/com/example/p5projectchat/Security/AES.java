package com.example.p5projectchat.Security;


import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class uses the Advanced encryption standard to handle security in the application
 */
public class AES {

    private static Cipher cipher, decipher;

    private static byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -60, 88, 17, 20, 3, -105, 119, -53};

    private static SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, "AES");


    public static String encrypt(String string){
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (NoSuchPaddingException e){
            e.printStackTrace();
        }

        byte[] stringByte = string.getBytes();
        byte[] encryptedStringByte = new byte[stringByte.length];

        try{
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedStringByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e){
            e.printStackTrace();
        } catch (BadPaddingException e){
            e.printStackTrace();
        } catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }

        String returnString = null;
        try{
            returnString = new String(encryptedStringByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return returnString;

    }

    public static String decrypt(String string) throws UnsupportedEncodingException{
        byte[] encryptedByte = string.getBytes("ISO-8859-1");

        String decryptedString = string;

        byte[] decryption;

        try{
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (NoSuchPaddingException e){
            e.printStackTrace();
        }

        try{
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(encryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e){
            e.printStackTrace();
        } catch (BadPaddingException e){
            e.printStackTrace();
        } catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }
        return decryptedString;

    }


}
