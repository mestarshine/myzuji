package com.myzuji.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * RSA 加密工具类
 *
 * @author shine
 * @date 2020/03/01
 */
public class RSAEncrypt {

    private static final String ALGORITHM = "RSA";

    /**
     * 最大明文长度 单位：byte
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * 最大密文长度 单位：byte
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密过程
     *
     * @param key     公钥/私钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(Key key, byte[] plainTextData)
            throws Exception {
        if (key == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] splitData = splitData(plainTextData, cipher, MAX_ENCRYPT_BLOCK);
            return Base64.encodeBase64String(splitData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param key     私钥/公钥
     * @param content 密文数据
     * @param encode  字符集编码
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(Key key, String content, String encode)
            throws Exception {
        if (key == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] data = Base64.decodeBase64(content);
            byte[] splitData = splitData(data, cipher, MAX_DECRYPT_BLOCK);
            return new String(splitData, encode);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    private static byte[] splitData(byte[] data, Cipher cipher, int maxLen) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int totalLen = data.length;
        int offSet = 0;
        int index = 0;
        byte[] cache;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 对数据分段解密
        while (totalLen - offSet > 0) {
            if (totalLen - offSet > maxLen) {
                cache = cipher.doFinal(data, offSet, maxLen);
            } else {
                cache = cipher.doFinal(data, offSet, totalLen - offSet);
            }
            out.write(cache, 0, cache.length);
            index++;
            offSet = index * maxLen;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }
}
