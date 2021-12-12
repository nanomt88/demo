package com.nanomt88.common.util.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-25 14:50
 **/
public class MD5Utils {

    public static String md5(String content) {
        return md5(content, null);
    }

    public static String md5(String content, String charset) {
        assert content != null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(StringUtils.getContentBytes(content, charset));
            String md5 = StringUtils.byteArrayToHex(digest);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5 计算摘要时异常：" + e.getMessage());
        }
    }

    /**
     * @param sign
     * @param content
     * @return
     */
    public static boolean verify(String sign, String content) {
        assert sign != null && content != null;
        String md5 = md5(content, null);
        return sign.equalsIgnoreCase(md5);
    }

    public static boolean verify(String sign, String content, String charset) {
        assert sign != null && content != null;
        String md5 = md5(content, charset);
        return sign.equalsIgnoreCase(md5);
    }

    /**
     * 签名字符串
     *
     * @param content       需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String content, String key, String input_charset) {
        content = content + key;
        return md5(content, input_charset);
    }

    /**
     * 签名字符串
     *
     * @param content       需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String sign, String content, String key, String input_charset) {
        content = content + key;
        return verify(content, sign, input_charset);
    }
}
