package com.nanomt88.common.util.security;

import org.junit.Test;

import javax.crypto.SecretKey;

import java.security.KeyPair;

import static org.junit.Assert.*;

public class DSAUtilsTest {
    @Test
    public void signTest() throws Exception {
        String msg = "123";
        KeyPair keyPair = DSAUtils.initKey();
        String sign = DSAUtils.sign(msg, keyPair.getPrivate());
        assert DSAUtils.verify(msg, sign, keyPair.getPublic());
    }

}