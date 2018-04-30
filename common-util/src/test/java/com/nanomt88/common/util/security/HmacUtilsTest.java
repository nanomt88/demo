package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;

import static com.nanomt88.common.util.security.HmacUtils.initKey;
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


}