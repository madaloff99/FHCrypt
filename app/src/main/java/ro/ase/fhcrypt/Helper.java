package ro.ase.fhcrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Helper {

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(password.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

        return hexString.toString();
    }

    public static SecretKey generateKey(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKey secret = new SecretKeySpec(password.getBytes(), "AES");
        return secret;
    }

    public static byte[] encryptMsg(String message, SecretKey secret, String algorithmName)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        byte[] cipherText;
        switch (algorithmName) {
            case "Blowfish":
                cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secret);
                cipherText = cipher.doFinal(message.getBytes("UTF8"));
                break;
            case "AES":
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secret);
                cipherText = cipher.doFinal(message.getBytes("UTF-8"));
                break;
            case "RC4":
                cipher = Cipher.getInstance("RC4/ECB/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, secret);
                cipherText = cipher.doFinal(message.getBytes("UTF-8"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algorithmName);
        }

        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret, String algorithmName)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        Cipher cipher = null;
        String decryptString = "";
        switch (algorithmName) {
            case "Blowfish":
                cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secret);
                decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
                break;
            case "AES":
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secret);
                decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
                break;
            case "RC4":
                cipher = Cipher.getInstance("RC4/ECB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, secret);
                decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algorithmName);
        }
        return decryptString;
    }
}
