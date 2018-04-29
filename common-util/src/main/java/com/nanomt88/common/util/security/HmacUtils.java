package com.nanomt88.common.util.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Hmac 算法工具类
 *
 * @author hongxudong
 * @create 2018-04-25 15:42
 **/
public class HmacUtils {

    private static final String HMACSHA256 = "HmacSHA256";
    private static final String HMACMD5 = "HmacMD5";


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
            byte[] hmacMessage = mac.doFinal(getContentBytes(content, charset));
            return StringUtils.byteArrayToHex(hmacMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param content
     * @param charset
     * @return
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
