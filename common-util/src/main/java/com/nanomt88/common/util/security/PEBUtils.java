package com.nanomt88.common.util.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PEBUtils {


    private static final String ENCRYPT_ALGORITHM = "PBEWithMD5AndDES";

    //偏移量，长度必须为8位
    private static final String PASSWORD = "87654321";

    private static final int CYCLE_COUNT = 100;

    public static SecretKey initKey() {
        return initKey(initSalt());
    }

    /**
     * 生成 加盐因子
     * @return
     */
    public static byte[] initSalt() {
        //生成加盐因子
        byte[] salt = new byte[8];//盐：Salt must be 8 bytes long
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * 初始化秘钥
     * @param salt  加盐
     * @return
     */
    public static SecretKey initKey(byte[] salt) {
        try {
            //生成私钥， 支持加密算法如下：PBEWithMD5AndDES、PBEWithMD5AndTripleDES、
            // PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPT_ALGORITHM);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(PASSWORD.toCharArray(),salt, CYCLE_COUNT,512);
            SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("PEB 初始化秘钥异常：" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("PEB 初始化秘钥异常：" + e.getMessage());
        }
    }

    /**
     * 加密
     *
     * @param content 待加密明文
     * @param key     秘钥
     * @return
     */
    public static String encrypt(String content, Key key, byte[] salt) {
        return encrypt(content, key,  salt,null);
    }

    /**
     * 加密
     *
     * @param content  待加密明文
     * @param keyBytes 秘钥
     * @return
     */
    public static String encrypt(String content, byte[] keyBytes, byte[] salt) {
        return encrypt(content, new SecretKeySpec(keyBytes, ENCRYPT_ALGORITHM),  salt,null);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, Key key, byte[] salt) {
        return decrypt(content, key,  salt,null);
    }

    /**
     * 解密
     *
     * @param content
     * @param keyBytes
     * @return
     */
    public static String decrypt(String content, byte[] keyBytes, byte[] salt) {
        return decrypt(content, new SecretKeySpec(keyBytes, ENCRYPT_ALGORITHM),  salt,null);
    }

    /**
     * 加密
     *
     * @param content 待加密明文
     * @param key     秘钥
     * @param charset 待加密明文和加密密文使用的字符集
     * @return
     */
    public static String encrypt(String content, Key key,byte[] salt,  String charset) {
        return Base64.encodeBase64String(
                doFinal(Cipher.ENCRYPT_MODE, StringUtils.getContentBytes(content, charset), key, salt));
    }

    /**
     * 解密
     *
     * @param content 待解密密文
     * @param key     秘钥
     * @param charset 待解密密文和原文使用的字符集
     * @return
     */
    public static String decrypt(String content, Key key, byte[] salt, String charset) {
        return StringUtils.toString(
                doFinal(Cipher.DECRYPT_MODE, Base64.decodeBase64String(content), key,  salt), charset);
    }

    private static byte[] doFinal(int mode, byte[] content, Key key, byte[] salt) {
        Cipher cipher;
        try {
            //加密    //循环次数：100
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, CYCLE_COUNT);
            //加解密 不指定时默认为：AES/ECB/PKCS5Padding（算法/模式/补码方式）
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(mode, key, paramSpec);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PEB 加解密 异常：" + e.getMessage());
        }
    }
}
