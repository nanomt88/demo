package com.nanomt88.common.util.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 *  DSA 非对称加密 工具类
 *
 * @author hongxudong
 * @create 2018-04-26 11:47
 **/
public class DSAUtils {

    private static final String DSA = "DSA";

    //偏移量，长度必须为8位
    private static final String IVPARAMETER = "12345678";

    public static SecretKey initKey(){
        SecureRandom secureRandom = new SecureRandom();
        return initKey(secureRandom);
    }

    public static SecretKey initKey(SecureRandom secureRandom){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(DSA);
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
     * 签名
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
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
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
