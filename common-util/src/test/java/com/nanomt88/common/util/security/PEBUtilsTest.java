package com.nanomt88.common.util.security;

import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.Assert.*;

public class PEBUtilsTest {
    @Test
    public void encryptTest() throws Exception {
        String msg = "123";
        SecretKey secretKey = PEBUtils.initKey();
        byte[] salt = PEBUtils.initSalt();
        String encrypt = PEBUtils.encrypt(msg, secretKey, salt);
        String decrypt = PEBUtils.decrypt(encrypt, secretKey, salt);
        assert msg.equals(decrypt);
    }

}