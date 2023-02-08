package com.dashboard.utils;
import lombok.extern.slf4j.Slf4j;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
@Slf4j
public class Encryption {
    public static String encryption(String password) throws NoSuchAlgorithmException {
        String encrypted;
        log.info("Logback before encryption  , use by message digest");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] bytes = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        encrypted = stringBuilder.toString();
        log.info("Logback  after encryption with MD5 algorithm in message digest library");
        return encrypted;
    }
}
