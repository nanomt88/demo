package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ${DESCRIPTION}
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-29 10:37
 **/
class DESUtilsTest {

    @Test
    void encryptTest() {
        SecretKey secretKey = DESUtils.initKey();
        String msg = "message";
        String encrypt = DESUtils.encrypt(msg, secretKey.getEncoded());
        String decrypt = DESUtils.decrypt(encrypt, secretKey);
        assert msg.equals(decrypt);
    }

}