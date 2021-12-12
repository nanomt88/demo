package com.nanomt88.common.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * SHA 摘要算法工具类
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-25 15:10
 **/
public class SHAUtils {

    private static String SHA256 = "SHA-256";
    private static String SHA1 = "SHA-1";

    /**
     * 计算sha256值
     *
     * @param content
     * @return
     */
    public static String sha256(String content) {
        return sha256(content, null);
    }

    /**
     * 计算sha256值
     *
     * @param content
     * @param charset
     * @return
     */
    public static String sha256(String content, String charset) {
        return digest(SHA256, content, charset);
    }

    /**
     * 计算 SHA-1 值
     *
     * @param content
     * @return
     */
    public static String sha1(String content) {
        return sha1(content, null);
    }

    /**
     * 计算 SHA-1 值
     *
     * @param content
     * @param charset
     * @return
     */
    public static String sha1(String content, String charset) {
        return digest(SHA1, content, charset);
    }

    /**
     * 验证 SHA-1 值 是否相等
     *
     * @param md5String
     * @param content
     * @param charset
     * @return
     */
    public static boolean verifySHA1(String md5String, String content, String charset) {
        assert md5String != null && content != null;
        String md5 = sha1(content, charset);
        return md5String.equalsIgnoreCase(md5);
    }

    /**
     * 验证 SHA-1 值 是否相等
     *
     * @param md5String
     * @param content
     * @return
     */
    public static boolean verifySHA1(String md5String, String content) {
        return verifySHA1(md5String, content, null);
    }

    /**
     * 验证 SHA-256 值 是否相等
     *
     * @param md5String
     * @param content
     * @param charset
     * @return
     */
    public static boolean verifySHA256(String md5String, String content, String charset) {
        assert md5String != null && content != null;
        String md5 = sha256(content, charset);
        return md5String.equalsIgnoreCase(md5);
    }

    /**
     * 验证 SHA-256 值 是否相等
     *
     * @param md5String
     * @param content
     * @return
     */
    public static boolean verifySHA256(String md5String, String content) {
        return verifySHA256(md5String, content, null);
    }

    private static String digest(String algorithm, String content, String charset) {
        assert content != null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(getContentBytes(content, charset));
            String sha = StringUtils.byteArrayToHex(digest);
            return sha;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("SHA 计算摘要时异常：" + e.getMessage());
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("SHA 计算摘要过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
