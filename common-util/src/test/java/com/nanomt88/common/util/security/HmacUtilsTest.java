package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-25 15:57
 **/
class HmacUtilsTest {
    @Test
    void hmacSHA256() {
        System.out.println(HmacUtils.hmacMD5("123", initKey()));
    }

    @Test
    void hmacMD5() {
        System.out.println(HmacUtils.hmacSHA256("123", initKey()));
    }

    private byte[] initKey(){
        //初始化KeyGenerator
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            //产生秘钥
            SecretKey secretKey = keyGenerator.generateKey();
            //获取秘钥
            byte[] key = secretKey.getEncoded();
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}