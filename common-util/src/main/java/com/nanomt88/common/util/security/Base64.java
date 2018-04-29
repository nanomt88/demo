package com.nanomt88.common.util.security;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-25 17:30
 **/
public class Base64 {

    public static String encodeBase64String(byte[] content){
        return java.util.Base64.getEncoder().encodeToString(content);
    }

    public static byte[] decodeBase64String(String content){
        return java.util.Base64.getDecoder().decode(content);
    }
}
