package com.nanomt88.common.util.security;


import javax.crypto.Cipher;
import java.security.*;

/**
 *  RSA 工具类
 *
 * @author hongxudong
 * @create 2018-04-25 17:13
 **/
public class RSAUtils {

    /**
     * 秘钥长度
     */
    private static final int KEY_LENGTH = 2048;
    /**
     * 加密算法
     */
    private static final String ENCRYPT_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHM = "SHA256withRSA";

    private static final String RSA = "RSA";

    /**
     * 初始化秘钥
     *
     * @return
     */
    public static KeyPair initKey() {
        return initKey(KEY_LENGTH);
    }

    /**
     * 初始化秘钥
     *
     * @param length
     * @return
     */
    public static KeyPair initKey(int length) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(length);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 初始化秘钥异常：" + e.getMessage());
        }
    }

    /**
     * 加密
     *
     * @param content   待加密明文
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String content, PublicKey publicKey) {
        return encrypt(content, publicKey, null);
    }

    /**
     * 解密
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String decrypt(String content, PrivateKey privateKey) {
        return decrypt(content, privateKey, null);
    }

    /**
     * 加密
     *
     * @param content   待加密明文
     * @param publicKey 公钥
     * @param charset   待加密明文和加密密文使用的字符集
     * @return
     */
    public static String encrypt(String content, PublicKey publicKey, String charset) {
        return Base64.encodeBase64String(
                doFinal(Cipher.ENCRYPT_MODE, StringUtils.getContentBytes(content, charset), publicKey));
    }

    /**
     * 解密
     *
     * @param content    待解密密文
     * @param privateKey 公钥
     * @param charset    待解密密文和原文使用的字符集
     * @return
     */
    public static String decrypt(String content, PrivateKey privateKey, String charset) {
        return StringUtils.toString(
                doFinal(Cipher.DECRYPT_MODE, Base64.decodeBase64String(content), privateKey), charset);
    }

    /**
     * 签名
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String sign(String content, PrivateKey privateKey) {
        return sign(content, privateKey, null);
    }

    /**
     * 签名
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     */
    public static String sign(String content, PrivateKey privateKey, String charset) {
        Signature signature;
        try {
            signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(StringUtils.getContentBytes(content, charset));
            byte[] sign = signature.sign();
            return Base64.encodeBase64String(sign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 计算签名时异常：" + e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 计算签名时异常：" + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 计算签名时异常：" + e.getMessage());
        }
    }

    /**
     * 验签
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean verify(String content, String sign, PublicKey publicKey) {
        return verify(content, sign, publicKey, null);
    }

    /**
     * 验签
     *
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     */
    public static boolean verify(String content, String sign, PublicKey publicKey, String charset) {
        //公钥验签
        Signature signature2 = null;
        try {
            signature2 = Signature.getInstance(SIGN_ALGORITHM);
            signature2.initVerify(publicKey);
            signature2.update(StringUtils.getContentBytes(content, charset));
            return signature2.verify(Base64.decodeBase64String(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 验证签名时异常：" + e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 验证签名时异常：" + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 验证签名时异常：" + e.getMessage());
        }
    }

    private static byte[] doFinal(int mode, byte[] content, Key key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(mode, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("RSA 加解密时异常：" + e.getMessage());
        }
    }
}
