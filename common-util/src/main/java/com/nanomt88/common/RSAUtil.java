package com.nanomt88.common;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


/**
 * RSA安全编码组件
 */
public abstract class RSAUtil {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data
     *            加密数据
     * @param privateKey
     *            Base64编码格式的私钥
     *
     * @return 经过Base64编码的字符串
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = HashUtil.decryptBASE64(privateKey);

        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return HashUtil.encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data
     *            加密数据
     * @param publicKey
     *            公钥
     * @param sign
     *            数字签名
     *
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {

        // 解密由base64编码的公钥
        byte[] keyBytes = HashUtil.decryptBASE64(publicKey);

        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(HashUtil.decryptBASE64(sign));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key Base64编码格式的私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key)
            throws Exception {
        byte[] decryptedData = null;

        // 对密钥解密
        byte[] keyBytes = HashUtil.decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int maxDecryptBlockSize = getMaxDencryptBytesByPrivate(keyFactory, privateKey);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            int dataLength = data.length;
            for (int i = 0; i < dataLength; i += maxDecryptBlockSize) {
                int decryptLength = dataLength - i < maxDecryptBlockSize ? dataLength - i : maxDecryptBlockSize;
                byte[] doFinal = cipher.doFinal(data, i, decryptLength);
                bout.write(doFinal);
            }
            decryptedData = bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }

        return decryptedData;

    }

    /**
     * 将Base64编码的密文解密为字符串
     * @param base64Str
     * @param key
     * @return
     * @throws Exception
     * @author renteng
     * @date 2015年12月24日 下午12:35:34
     */
    public static String decryptByPrivateKeyToString(String base64Str, String key) throws Exception{
        byte[] data = HashUtil.decryptBASE64(base64Str);
        byte[] oriData = decryptByPrivateKey(data, key);

        return new String(oriData);
    }

    /**
     * 获取公钥加密可加密的最大数据字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getMaxEncryptBytesByPublicKey(KeyFactory keyFactory, Key key){
        return getPublicKeyBitLength(keyFactory, key) / 8 - 11;
    }

    /**
     * 获取公钥解密每块的字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getMaxDencryptBytesByPrivate(KeyFactory keyFactory, Key key){
        return getPrivateKeyBitLength(keyFactory, key) / 8;
    }

    /**
     * 获取公钥加密可加密的最大数据字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getMaxEncryptBytesByPrivate(KeyFactory keyFactory, Key key){
        return getPrivateKeyBitLength(keyFactory, key) / 8 - 11;
    }

    /**
     * 获取公钥解密每块的字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getMaxDencryptBytesByPublicKey(KeyFactory keyFactory, Key key){
        return getPublicKeyBitLength(keyFactory, key) / 8;
    }

    /**
     * 获取公钥的字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getPublicKeyBitLength(KeyFactory keyFactory, Key key){
        try {
            RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(key, RSAPublicKeySpec.class);
            return publicKeySpec.getModulus().bitLength();
        } catch (Exception e) {

        }
        return 2048;
    }

    /**
     * 获取私钥的字节长度
     * @param keyFactory
     * @param key
     * @return
     */
    private static int getPrivateKeyBitLength(KeyFactory keyFactory, Key key){
        try {
            RSAPrivateKeySpec publicKeySpec = keyFactory.getKeySpec(key, RSAPrivateKeySpec.class);
            return publicKeySpec.getModulus().bitLength();
        } catch (Exception e) {

        }

        return 2048;
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key Base64编码格式的公钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        byte[] decryptedData = null;

        // 对密钥解密
        byte[] keyBytes = HashUtil.decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        int maxDecryptBlockSize = getMaxDencryptBytesByPublicKey(keyFactory, publicKey);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            int dataLength = data.length;
            for (int i = 0; i < dataLength; i += maxDecryptBlockSize) {
                int decryptLength = dataLength - i < maxDecryptBlockSize ? dataLength - i
                        : maxDecryptBlockSize;
                byte[] doFinal = cipher.doFinal(data, i, decryptLength);
                bout.write(doFinal);
            }
            decryptedData = bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }

        return decryptedData;
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key Base64编码格式的公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws Exception {
        byte[] encryptedData = null;
        // 对公钥解密
        byte[] keyBytes = HashUtil.decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int maxEncryptBlockSize = getMaxEncryptBytesByPublicKey(keyFactory, publicKey);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            int dataLength = data.length;
            for (int i = 0; i < data.length; i += maxEncryptBlockSize) {
                int encryptLength = dataLength - i < maxEncryptBlockSize ? dataLength - i
                        : maxEncryptBlockSize;
                byte[] doFinal = cipher.doFinal(data, i, encryptLength);
                bout.write(doFinal);
            }
            encryptedData = bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }

        return encryptedData;
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data 密文二进制数据
     * @param key BASE64编码的私钥字符串
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        byte[] encryptedData = null;

        // 对密钥解密
        byte[] keyBytes = HashUtil.decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        int maxEncryptBlockSize = getMaxEncryptBytesByPrivate(keyFactory, privateKey);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            int dataLength = data.length;
            for (int i = 0; i < data.length; i += maxEncryptBlockSize) {
                int encryptLength = dataLength - i < maxEncryptBlockSize ? dataLength - i
                        : maxEncryptBlockSize;
                byte[] doFinal = cipher.doFinal(data, i, encryptLength);
                bout.write(doFinal);
            }
            encryptedData = bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }

        return encryptedData;
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return HashUtil.encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return HashUtil.encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
}