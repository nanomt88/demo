package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ${DESCRIPTION}
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-25 17:15
 **/
class AESUtilsTest {
    @Test
    void aseEncrypt() {
        String msg = "123";
        byte[] key = AESUtils.initKey();
        String encrypt = AESUtils.encrypt(msg, key);
        System.out.println(encrypt);
        String decrypt = AESUtils.decrypt(encrypt, key);
        System.out.println(decrypt);
        assert msg.equals(decrypt);
    }

}