package com.nanomt88.common.util.security;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

import static com.nanomt88.common.util.security.StringUtils.getContentBytes;

/**
 * ${DESCRIPTION}
 *
 * @author hongxudong
 * @create 2018-04-25 16:00
 **/
public class AESUtils {

    /**
     * 偏移量
     */
    private static final String IVPARAMETER = "0102030405060708";
    /**
     * AES 加密算法
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";

    /**
     * aes 加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encrypt(String content, byte[] key) {
        return encrypt(content, key, null);
    }

    /**
     * aes 加密
     *
     * @param content
     * @param key
     * @param charset
     * @return
     */
    public static String encrypt(String content, byte[] key, String charset) {
        return Base64.encodeBase64String(doFinal(Cipher.ENCRYPT_MODE, getContentBytes(content, charset), key, charset));

    }

    /**
     * aes 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, byte[] key) {
        return decrypt(content, key, null);
    }

    /**
     * aes 解密
     *
     * @param content
     * @param key
     * @param charset
     * @return
     */
    public static String decrypt(String content, byte[] key, String charset) {
        return StringUtils.toString(doFinal(Cipher.DECRYPT_MODE, Base64.decodeBase64String(content), key, charset), charset);
    }

    public static byte[] initKey() {
        return initKey(256);
    }

    public static byte[] initKey(int length) {
        //初始化KeyGenerator
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(AES);
            //注意因默认jdk限制，不能使用256位长度，需要替换${java_home}\jre6\lib\security
            //目录下的local_policy.jar、US_export_policy.jar（使用policy/unlimited下的即可）
            keyGenerator.init(length <= 0 ? 256 : length);
            //产生秘钥
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("AES 初始化秘钥异常：" + e.getMessage());
        }
    }

    private static byte[] doFinal(int algorithm, byte[] content, byte[] key, String charset) {
        IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
        //还原秘钥
        SecretKey secretKey = new SecretKeySpec(key, AES);
        //加密 不指定时默认为：AES/ECB/PKCS5Padding（算法/模式/补码方式）
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(algorithm, secretKey, iv);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AES 加解密 异常：" + e.getMessage());
        }
    }
}
