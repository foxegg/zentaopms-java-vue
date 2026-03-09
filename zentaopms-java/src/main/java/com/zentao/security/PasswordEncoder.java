package com.zentao.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码编码器 - 兼容 ZenTao PHP 的 MD5 存储
 */
public final class PasswordEncoder {

    private PasswordEncoder() {}

    public static String encode(String rawPassword) {
        return md5(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return md5(rawPassword).equals(encodedPassword);
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }
}
