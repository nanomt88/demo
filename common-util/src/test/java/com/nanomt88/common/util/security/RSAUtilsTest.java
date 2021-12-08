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
        String str = "1111122222";
        KeyPair keyPair = initKey();
        String encrypt = encrypt(str, keyPair.getPublic());
        String decrypt = decrypt(encrypt, keyPair.getPrivate());

        assert  str.equals(decrypt);

        //私钥可以加密，但是有数据长度限制，不能超过秘钥长度-padding
        String encrypt2 = encrypt(str, keyPair.getPrivate());
        String decrypt2 = decrypt(encrypt2, keyPair.getPublic());
        assert str.equals(decrypt2);
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