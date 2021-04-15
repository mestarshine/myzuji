package com.myzuji.util.security;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

/**
 * 获取 RSA 密钥 工具类
 *
 * @author shine
 * @date 2020/03/01
 */
public class RSAObtainKey {

    private static final String ALGORITHM = "RSA";

    /**
     * 从文件中输入流中加载公钥
     *
     * @param path 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static String loadPublicKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/publicKey.keystore"));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 读取公钥cer
     *
     * @param path .cer文件的路径  如：c:/abc.cer
     * @return base64后的公钥串
     * @throws IOException
     * @throws CertificateException
     */
    public static String getPublicKey(String path) throws IOException,
        CertificateException {
        InputStream inStream = new FileInputStream(path);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = inStream.read()) != -1) {
            out.write(ch);
        }
        return Base64.encodeBase64String(out.toByteArray());
    }

    /**
     * 从文件中输入流中加载私钥
     *
     * @param path 私钥文件名
     * @return 私钥字符串
     * @throws Exception
     */
    public static String loadPrivateKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/privateKey.keystore"));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr 私钥数据字符串
     * @throws Exception 加载私钥时产生的异常
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
        throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 读取私钥  返回PrivateKey
     *
     * @param path     包含私钥的证书路径
     * @param password 私钥证书密码
     * @return 返回私钥PrivateKey
     */
    public static RSAPrivateKey loadPrivateKeyByCertificate(String path, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(path);
        char[] nPassword = null;
        if ((password == null) || password.trim().equals("")) {
            nPassword = null;
        } else {
            nPassword = password.toCharArray();
        }
        ks.load(fis, nPassword);
        fis.close();
        Enumeration<String> en = ks.aliases();
        String keyAlias = null;
        if (en.hasMoreElements()) {
            keyAlias = (String) en.nextElement();
        }
        return (RSAPrivateKey) ks.getKey(keyAlias, nPassword);

    }
}
