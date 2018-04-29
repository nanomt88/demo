package com.nanomt88.common.util.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * DSA 非对称加密 工具类
 *
 * @author hongxudong
 * @create 2018-04-26 11:47
 **/
public class DSAUtils {

    private static final String DSA = "DSA";


    public static KeyPair initKey() {
        SecureRandom secureRandom = new SecureRandom();
        return initKey(secureRandom);
    }

    public static KeyPair initKey(SecureRandom secureRandom) {
        try {
            //初始化秘钥生成器
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(DSA);
            keyGenerator.initialize(1024, secureRandom);
            //生成秘钥
            KeyPair keyPair = keyGenerator.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("DSA 初始化秘钥异常：" + e.getMessage());
        }
    }

    /**
     * 签名
     *
     * @param content
     * @param privateKeyBytes
     * @return
     */
    public static String sign(String content, byte[] privateKeyBytes) {
        //还原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(DSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return sign(content, privateKey, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("DSA 签名 异常：" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 签名 异常：" + e.getMessage());
        }
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
            signature = Signature.getInstance(DSA);
            signature.initSign(privateKey);
            signature.update(StringUtils.getContentBytes(content, charset));
            byte[] sign = signature.sign();
            return Base64.encodeBase64String(sign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 签名 异常：" + e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 签名 异常：" + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 签名 异常：" + e.getMessage());
        }
    }

    /**
     * 验签
     *
     * @param content
     * @param sign
     * @param publicKeyBytes
     * @return
     */
    public static boolean verify(String content, String sign, byte[] publicKeyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory2 = null;
        try {
            keyFactory2 = KeyFactory.getInstance(DSA);
            PublicKey publicKey = keyFactory2.generatePublic(x509EncodedKeySpec);
            return verify(content, sign, publicKey, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 验签 异常：" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 验签 异常：" + e.getMessage());
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
            signature2 = Signature.getInstance(DSA);
            signature2.initVerify(publicKey);
            signature2.update(StringUtils.getContentBytes(content, charset));
            return signature2.verify(Base64.decodeBase64String(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 验签 异常：" + e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 验签 异常：" + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("DES 验签 异常：" + e.getMessage());
        }
    }
}
