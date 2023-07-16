package com.coderman.auth.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class PasswordUtils {

    public static String encryptMD5(String input)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
           log.error("encryptMD5 error:{}",e.getMessage(),e);
        }
        assert md != null;
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    public static String encryptSHA256(String input)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("encryptSHA256 error:{}",e.getMessage(),e);
        }
        assert md != null;
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String originalPassword = "123456";

        String md5Hash = encryptMD5(originalPassword);
        String sha256Hash = encryptSHA256(originalPassword);

        System.out.println("MD5 Hash: " + md5Hash);
        System.out.println("SHA-256 Hash: " + sha256Hash);
    }
}
