package com.nanomt88.common.util.security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Hmac 算法工具类
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-25 15:42
 **/
public class HmacUtils {

    private static final String HMACSHA256 = "HmacSHA256";
    private static final String HMACMD5 = "HmacMD5";

    public static byte[] initKey(){
        //初始化KeyGenerator
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            //产生秘钥
            SecretKey secretKey = keyGenerator.generateKey();
            //获取秘钥
            byte[] key = secretKey.getEncoded();
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("HMAC 初始化秘钥异常：" + e.getMessage());
        }
    }

    public static String hmacSHA256(String content, byte[] key) {
        return hmac(HMACSHA256, content, key, null);
    }

    public static String hmacSHA256(String content, byte[] key, String charset) {
        return hmac(HMACSHA256, content, key, charset);
    }

    public static String hmacMD5(String content, byte[] key) {
        return hmac(HMACMD5, content, key, null);
    }

    public static String hmacMD5(String content, byte[] key, String charset) {
        return hmac(HMACMD5, content, key, charset);
    }

    private static String hmac(String algorithm, String content, byte[] key, String charset) {
        //还原秘钥 new SecretKeySpec(PRIVATE_KEY.getBytes(), "HmacSHA256")
        //HmacMD5、HmacSHA256
        SecretKeySpec restoreSecreKey = new SecretKeySpec(key, algorithm);
        //实例化MAC
        Mac mac = null;
        try {
            mac = Mac.getInstance(restoreSecreKey.getAlgorithm());
            mac.init(restoreSecreKey);
            byte[] hmacMessage = mac.doFinal(StringUtils.getContentBytes(content, charset));
            return StringUtils.byteArrayToHex(hmacMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("hmac计算摘要时异常：" + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("hmac计算摘要时异常：" + e.getMessage());
        }
    }
}
