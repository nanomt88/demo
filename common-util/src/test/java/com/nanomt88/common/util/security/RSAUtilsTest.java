package com.nanomt88.common.util.security;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static com.nanomt88.common.util.security.RSAUtils.*;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-25 18:07
 **/
class RSAUtilsTest {
    @Test
    void encrypt1() {
        String str = "123";
        KeyPair keyPair = initKey();
        String encrypt = encrypt(str, keyPair.getPublic());
        String decrypt = decrypt(encrypt, keyPair.getPrivate());
        assert  str.equals(decrypt);
    }

    @Test
    void sign1(){
        String msg = "123";
        KeyPair keyPair = initKey();
        String sign = sign(msg, keyPair.getPrivate());
        boolean verify = verify(msg, sign, keyPair.getPublic());
        assert verify;
    }

}