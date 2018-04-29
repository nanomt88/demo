package com.nanomt88.common.util.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 *  DES 对称加密 工具类
 *
 * @author hongxudong
 * @create 2018-04-26 11:47
 **/
public class DESUtils {

    private static final String DES = "DES";
    private static final String ENCRYPT_ALGORITHM = "DES/CBC/PKCS5Padding";

    //偏移量，长度必须为8位
    private static final String IVPARAMETER = "12345678";

    public static SecretKey initKey(){
        SecureRandom secureRandom = new SecureRandom();
        return initKey(secureRandom);
    }

    public static SecretKey initKey(SecureRandom secureRandom){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(DES);
            keyGenerator.init(secureRandom);
            //产生秘钥
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param content   待加密明文
     * @param key 公钥
     * @return
     */
    public static String encrypt(String content, Key key) {
        return encrypt(content, key, null);
    }

    /**
     * 加密
     *
     * @param content   待加密明文
     * @param keyBytes 公钥
     * @return
     */
    public static String encrypt(String content, byte[] keyBytes) {
        return encrypt(content, new SecretKeySpec(keyBytes, DES), null);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, Key key) {
        return decrypt(content, key, null);
    }

    /**
     * 解密
     *
     * @param content
     * @param keyBytes
     * @return
     */
    public static String decrypt(String content,  byte[] keyBytes) {
        return decrypt(content, new SecretKeySpec(keyBytes, DES), null);
    }

    /**
     * 加密
     *
     * @param content   待加密明文
     * @param key 公钥
     * @param charset   待加密明文和加密密文使用的字符集
     * @return
     */
    public static String encrypt(String content, Key key, String charset) {
        return Base64.encodeBase64String(
                doFinal(Cipher.ENCRYPT_MODE, StringUtils.getContentBytes(content, charset), key));
    }

    /**
     * 解密
     *
     * @param content    待解密密文
     * @param key 公钥
     * @param charset    待解密密文和原文使用的字符集
     * @return
     */
    public static String decrypt(String content, Key key, String charset) {
        return StringUtils.toString(
                doFinal(Cipher.DECRYPT_MODE, Base64.decodeBase64String(content), key), charset);
    }

    private static byte[] doFinal(int mode, byte[] content, Key key) {
        Cipher cipher;
        try {
            //偏移量
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
            //加解密 不指定时默认为：AES/ECB/PKCS5Padding（算法/模式/补码方式）
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(mode, key, iv);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
