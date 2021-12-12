package com.nanomt88.common.util.security;


import com.nanomt88.common.HashUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * ${DESCRIPTION}
 *
 * @author nanomt88@gmail.com
 * @create 2018-04-10 14:45
 **/
public class EncryptTest {

    @Test
    public void md5() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest("123".getBytes());
        String md5 = byteArrayToHex(digest);
        System.out.println(md5);

        System.out.println(Base64.encodeBase64String(digest));
    }

    @Test
    public void hmac_sha256() throws Exception {
        String message = "123";
        //初始化KeyGenerator
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        //产生秘钥
        SecretKey secretKey = keyGenerator.generateKey();
        //获取秘钥
        byte[] key = secretKey.getEncoded();
        System.out.println(Base64.encodeBase64String(key));

        //还原秘钥 ，或使用已经定义好的秘钥  new SecretKeySpec(PRIVATE_KEY.getBytes(), "HmacSHA256")
        SecretKeySpec restoreSecreKey = new SecretKeySpec(key, "HmacSHA256"); //HmacMD5、HmacSHA256
        //实例化MAC
        Mac mac = Mac.getInstance(restoreSecreKey.getAlgorithm());
        mac.init(restoreSecreKey);
        byte[] hmacMessage = mac.doFinal(message.getBytes());
        System.out.println(byteArrayToHex(hmacMessage));

        System.out.println(byteArrayToHex(HashUtil.encryptHMAC(message.getBytes(),Base64.encodeBase64String(key))));
    }

    @Test
    public void aes() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        String message = "123";
        //初始化KeyGenerator
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //注意因默认jdk限制，不能使用256位长度，需要替换${java_home}\jre6\lib\security
        //目录下的local_policy.jar、US_export_policy.jar（使用policy/unlimited下的即可）
        keyGenerator.init(256);
        //产生秘钥
        SecretKey secretKey = keyGenerator.generateKey();
        //偏移量
        String IVPARAMETER = "0102030405060708";
        IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
        //获取秘钥
        byte[] keyBytes = secretKey.getEncoded();
        System.out.println(Base64.encodeBase64String(keyBytes));
        //还原秘钥 ，或使用有秘钥new SecretKeySpec(PRIVATE_KEY.getBytes(), "AES")
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        //加密 不指定时默认为：AES/ECB/PKCS5Padding（算法/模式/补码方式）
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);
        byte[] result = cipher.doFinal(message.getBytes());
        System.out.println(Base64.encodeBase64String(result));
        System.out.println(byteArrayToHex(result));
        //解密
        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, key, iv);
        System.out.println(new String(cipher2.doFinal(result)));
    }

    /**
     * RSA 公钥加密 -- 私钥解密
     */
    @Test
    public void rsaEncrypt1() throws Exception{
        String message = "123";
        //初始化KeyGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("公钥：" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println(rsaPublicKey.getFormat());
        System.out.println("私钥：" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        System.out.println(rsaPrivateKey.getFormat());
        //公钥加密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] result = cipher.doFinal(message.getBytes());
        System.out.println(Base64.encodeBase64String(result));
        //私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory2.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher2.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] src = cipher2.doFinal(result);
        System.out.println(new String(src));
    }

    /**
     * RSA 私钥加密 -- 公钥解密
     */
    @Test
    public void rsaEncrypt2() throws Exception {
        String message = "123";
        //初始化KeyGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("公钥：" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        //私钥加密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(message.getBytes());
        System.out.println("密文："+Base64.encodeBase64String(result));
        //公钥解密
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory2.generatePublic(x509EncodedKeySpec);
        Cipher cipher2 = Cipher.getInstance("RSA");
        cipher2.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] src = cipher2.doFinal(result);
        System.out.println("明文："+new String(src));
    }

    @Test
    public void peb() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String message = "123";
        //密码
        String password = "12345678";
        //生成加盐因子
        byte[] salt = new byte[8];//盐：Salt must be 8 bytes long
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        //生成私钥， 支持加密算法如下：PBEWithMD5AndDES、PBEWithMD5AndTripleDES、
        // PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),salt, 100,512);
        SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
        System.out.println("秘钥："+Base64.encodeBase64String(secretKey.getEncoded()));
        //加密    //循环次数：100
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] result = cipher.doFinal(message.getBytes());
        System.out.println("密文："+Base64.encodeBase64String(result));
        //解密
        Cipher cipher2 = Cipher.getInstance("PBEWithMD5AndDES");
        cipher2.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        byte[] src = cipher2.doFinal(result);
        System.out.println("明文："+ new String(src));
    }

    /**
     * RSA 签名和验签
     */
    @Test
    public void rsaSign() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String message = "123";
        //初始化KeyGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("公钥：" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
        //私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //使用私钥签名
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] sign = signature.sign();
        System.out.println("签名结果："+Base64.encodeBase64String(sign));
        //公钥
        X509EncodedKeySpec  x509EncodedKeySpec = new X509EncodedKeySpec (rsaPublicKey.getEncoded());
        KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory2.generatePublic(x509EncodedKeySpec);
        //公钥验签
        Signature signature2 = Signature.getInstance("SHA256withRSA");
        signature2.initVerify(publicKey);
        signature2.update(message.getBytes());
        System.out.println("验签结果："+signature2.verify(sign));
    }

    @Test
    public void des()throws Exception{
        String message = "123";
        //初始化KeyGenerator
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(secureRandom);
        //产生秘钥
        SecretKey secretKey = keyGenerator.generateKey();
        //获取秘钥
        byte[] keyBytes = secretKey.getEncoded();
        System.out.println("秘钥：" + Base64.encodeBase64String(keyBytes));
        //还原秘钥 ，或使用有秘钥new SecretKeySpec(PRIVATE_KEY.getBytes(), "AES")
        SecretKey key = new SecretKeySpec(keyBytes, "DES");
        //偏移量，长度必须为8位
        String IVPARAMETER = "12345678";
        IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
        //加密 不指定时默认为：AES/ECB/PKCS5Padding（算法/模式/补码方式）
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] result = cipher.doFinal(message.getBytes());
        System.out.println("密文："+Base64.encodeBase64String(result));
        //解密
        Cipher cipher2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher2.init(Cipher.DECRYPT_MODE, key, iv);
        System.out.println("原文："+new String(cipher2.doFinal(result)));
    }

    /**
     *  数字签名算法DSA
     * @throws Exception
     */
    @Test
    public void dsa()throws Exception{
        String message = "123";
        //初始化秘钥生成器
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
        //初始化随机数生成器
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.initialize(1024, secureRandom);
        //生成秘钥
        KeyPair keyPair = keyGenerator.generateKeyPair();
        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
        System.out.println("公钥：" + Base64.encodeBase64String(publicKey.getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64String(privateKey.getEncoded()));
        //还原私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //加签
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
        signature.initSign(privateKey1);
        signature.update(message.getBytes());
        byte[] sign = signature.sign();
        System.out.println("签名：\n" + Base64.encodeBase64String(sign));
        //还原公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        KeyFactory keyFactory2 = KeyFactory.getInstance("DSA");
        PublicKey publicKey1 = keyFactory2.generatePublic(x509EncodedKeySpec);
        //验签
        Signature signature2 = Signature.getInstance(keyFactory2.getAlgorithm());
        signature2.initVerify(publicKey1);
        signature2.update(message.getBytes());
        System.out.println("校验结果：" + signature2.verify(sign));
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }
}
