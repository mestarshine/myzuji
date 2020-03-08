package com.myzuji.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES 工具类
 *
 * @author shine
 * @date 2020/03/01
 */
public class AESEncrypt {

    private static final String ALGORITHM = "AES";
    /**
     * CBC对于每个待加密的密码块在加密前会先与前一个密码块的密文异或然后再用加密器加密
     * 第一个明文块与一个叫初始化向量的数据块异或
     */
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * ECB加密前根据加密块大小（如AES为128位）分成若干块，之后将每块使用相同的密钥单独加密，解密同理。
     */
    private static final String AES_ECB_PKCS5_PADDING = "AES/ECB/PKCS5Padding";

    public static String generateAESKey4CBC() throws NoSuchAlgorithmException {
        //KeyGenerator提供对称密钥生成器的功能，支持各种算法
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        //SecretKey负责保存对称密钥
        SecretKey deskey = keygen.generateKey();
        return new String(Base64.encodeBase64(deskey.getEncoded()));
    }

    public static String generateAESKey4ECB() throws Exception {
        //KeyGenerator提供对称密钥生成器的功能，支持各种算法
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        //SecretKey负责保存对称密钥
        keygen.init(128);
        String key = Base64.encodeBase64String(keygen.generateKey().getEncoded());
        return key;
    }

    /**
     * AES ecb加密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptECB(String content, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptBytes = cipher.doFinal(content.getBytes("UTF-8"));
        return Base64.encodeBase64String(encryptBytes);
    }

    /**
     * AES ecb解密
     *
     * @param content 密文
     * @param key     aes秘钥
     * @return
     * @throws Exception
     */
    public static String decryptECB(String content, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
        Cipher deCipher = Cipher.getInstance(AES_ECB_PKCS5_PADDING);
        deCipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] bytes = deCipher.doFinal(Base64.decodeBase64(content.getBytes("UTF-8")));
        return new String(bytes, "UTF-8");
    }

    /**
     * CBC模式AES加密
     *
     * @param content 待加密明文
     * @param key     aes秘钥
     * @param charset 字符集
     * @return
     * @throws Exception
     */
    public static String encryptCBC(String content, String key, String charset) throws Exception {
        //反序列化AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(initIv(AES_CBC_PKCS5_PADDING));
        //初始化加密器并加密
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
        return new String(Base64.encodeBase64(encryptBytes));
    }

    /**
     * CBC模式AES解密
     *
     * @param content 密文
     * @param key     aes密钥
     * @param charset 字符集
     * @return 原文
     * @throws Exception
     */
    public static String decryptCBC(String content, String key, String charset) throws Exception {
        //反序列化AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(key.getBytes()), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(initIv(AES_CBC_PKCS5_PADDING));
        //初始化加密器并加密
        Cipher deCipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        deCipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] encryptedBytes = Base64.decodeBase64(content.getBytes(charset));
        byte[] bytes = deCipher.doFinal(encryptedBytes);
        return new String(bytes);
    }

    /**
     * 初始向量的方法, 全部为0. 这里的写法适合于其它算法,针对AES算法的话,IV值一定是128位的(16字节).
     *
     * @param fullAlg
     * @return
     * @throws GeneralSecurityException
     */
    private static byte[] initIv(String fullAlg) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(fullAlg);
        int blockSize = cipher.getBlockSize();
        byte[] iv = new byte[blockSize];
        for (int i = 0; i < blockSize; ++i) {
            iv[i] = 0;
        }
        return iv;
    }

    /**
     * AES自定义密钥加密
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String password) throws Exception {
        SecretKey secretKey = createCipher(password);
        SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(content.getBytes("utf-8"));
        return new String(Base64.encodeBase64(result));
    }

    /**
     * AES自定义密钥解密
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String password) throws Exception {
        SecretKey secretKey = createCipher(password);
        SecretKeySpec key = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(Base64.decodeBase64(content.getBytes()));
        return new String(result);
    }

    private static SecretKey createCipher(String pwd) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128, new SecureRandom(pwd.getBytes()));
        return keyGenerator.generateKey();
    }

    public static void main(String[] args) throws Exception {
        String plainText = "342534536242364562424";
        System.out.println(encrypt(plainText, "##!#$!#$DSFA#$!DFA#"));
    }
}
