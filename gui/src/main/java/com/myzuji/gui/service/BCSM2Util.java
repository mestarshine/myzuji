package com.myzuji.gui.service;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Enumeration;


/**
 * 利用 BC 封装的SM2 工具类
 */
public class BCSM2Util {
    private static final String ALGORITHM = "SM2";
    private static BouncyCastleProvider provider;
    private static X9ECParameters parameters;
    private static ECParameterSpec ecParameterSpec;
    private static KeyFactory keyFactory;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
        }
        try {
            provider = new BouncyCastleProvider();
            parameters = GMNamedCurves.getByName("sm2p256v1");
            ecParameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
            keyFactory = KeyFactory.getInstance("EC", provider);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * base64 格式的私钥转为Hex格式
     *
     * @param priKeyBase64 base64格式的私钥
     * @return 返回 Hex 格式的私钥
     */
    public static String base64ToHexKey(String priKeyBase64) {
        byte[] encoding = java.util.Base64.getDecoder().decode(priKeyBase64);
        ASN1Sequence seq = ASN1Sequence.getInstance(encoding);
        DEROctetString context = (DEROctetString) seq.getObjectAt(2);
        ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(context.getOctets());
        ASN1Encodable value = asn1Sequence.getObjectAt(1);
        BigInteger d = ((ASN1Integer) value).getPositiveValue();
        return d.toString(16).toUpperCase();
    }

    /**
     * 数字信封加密
     *
     * @param input  明文
     * @param pubKey Hex 格式的公钥
     * @return 返回加密结果
     */
    public static String encodeDgtlEnvlp(String input, String pubKey) throws Exception {
        return encrypt(Base64.decode(input), Hex.decode(pubKey));
    }

    /**
     * 加密
     *
     * @param input  明文
     * @param pubKey Base64 格式的公钥
     * @return 返回加密结果
     */
    public static String encryptBase64(String input, String pubKey) throws Exception {
        return encrypt(input.getBytes(StandardCharsets.UTF_8), Base64.decode(pubKey));
    }

    /**
     * 加密
     *
     * @param input  明文
     * @param pubKey Hex 格式的公钥
     * @return 返回加密结果
     */
    public static String encryptHex(String input, String pubKey) throws Exception {
        return encrypt(input.getBytes(StandardCharsets.UTF_8), Hex.decode(pubKey));
    }

    /**
     * 加密
     *
     * @param input  明文
     * @param pubKey 公钥
     * @return 返回加密结果
     */
    private static String encrypt(byte[] input, byte[] pubKey) throws Exception {
        ECPoint ecPoint = parameters.getCurve().decodePoint(pubKey);
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        Cipher cipher = Cipher.getInstance(ALGORITHM, provider);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = cipher.doFinal(input);
        return Base64.toBase64String(derEncode(cipherConversion123TO132(data)));
    }

    /**
     * base64 格式的私钥解密
     *
     * @param cipherData   密文字节 Base64 或 Hex 处理后的结果
     * @param priKeyBase64 Base64 格式私钥
     * @return 返回解密结果
     */
    public static String decryptBase64(byte[] cipherData, String priKeyBase64) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String priKeyHex = base64ToHexKey(priKeyBase64);
        return decryptHex(cipherData, priKeyHex);
    }

    /**
     * 解密
     *
     * @param cipherData 密文字节 Base64 或 Hex 处理后的结果
     * @param priKeyHex  Hex 格式私钥
     * @return 返回解密结果
     */
    public static String decryptHex(byte[] cipherData, String priKeyHex) throws NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        final BigInteger key = new BigInteger(priKeyHex, 16);
        return decrypt(cipherData, key);
    }

    /**
     * 解密
     *
     * @param data   密文
     * @param prvKey 私钥
     * @return 返回解密结果
     */
    private static String decrypt(byte[] data, BigInteger prvKey) throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM, provider);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(prvKey, ecParameterSpec));
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] dataFormat132 = decodeDERSM2Cipher(data);
        byte[] dataFormat123 = cipherConversion132TO123(dataFormat132);

        return Hex.toHexString(cipher.doFinal(dataFormat123)).toUpperCase();
    }

    /**
     * 密文从123模式转为132
     *
     * @param cipherData 密文byte[]
     * @return 转换后的byte[]
     */
    private static byte[] cipherConversion123TO132(byte[] cipherData) {
        byte[] input = Arrays.copyOfRange(cipherData, 1, cipherData.length);
        byte[] convertBytes = doCipherConversion123TO132(input);
        byte[] output = new byte[cipherData.length];
        output[0] = 0x04;
        System.arraycopy(convertBytes, 0, output, 1, convertBytes.length);
        return output;
    }

    /**
     * 密文从123模式转为132
     *
     * @param cipherData 密文byte[]
     * @return 转换后的byte[]
     */
    private static byte[] doCipherConversion123TO132(byte[] cipherData) {
        int length = cipherData.length;
        byte[] output = new byte[cipherData.length];
        System.arraycopy(cipherData, 0, output, 0, 64);
        System.arraycopy(cipherData, length - 32, output, 64, 32);
        System.arraycopy(cipherData, 64, output, 96, length - 96);
        return output;
    }

    /**
     * 密文从132模式转为123
     *
     * @param cipherData 密文byte[]
     * @return 转换后的byte[]
     */
    private static byte[] cipherConversion132TO123(byte[] cipherData) {

        byte[] input = Arrays.copyOfRange(cipherData, 1, cipherData.length);
        byte[] output = new byte[cipherData.length];
        output[0] = 0x04;
        byte[] convertBytes = doCipherConversion132TO123(input);
        System.arraycopy(convertBytes, 0, output, 1, convertBytes.length);
        return output;
    }

    /**
     * 密文从132模式转为123
     *
     * @param cipherData 密文byte[]
     * @return 转换后的byte[]
     */
    private static byte[] doCipherConversion132TO123(byte[] cipherData) {
        int length = cipherData.length;
        byte[] output = new byte[length];
        output[0] = 0x04;

        System.arraycopy(cipherData, 0, output, 0, 64);
        System.arraycopy(cipherData, 96, output, 64, length - 96);
        System.arraycopy(cipherData, 64, output, length - 32, 32);

        return output;
    }

    /**
     * 解密
     *
     * @param cipher 密文
     * @return 返回加密结果
     */
    private static byte[] derEncode(byte[] cipher) throws Exception {
        byte[] c1x = new byte[32];
        byte[] c1y = new byte[32];
        byte[] c2 = new byte[cipher.length - c1x.length - c1y.length - 1 - 32];
        byte[] c3 = new byte[32];

        int startPos = 1;
        System.arraycopy(cipher, startPos, c1x, 0, c1x.length);
        startPos += c1x.length;
        System.arraycopy(cipher, startPos, c1y, 0, c1y.length);
        startPos += c1y.length;


        ASN1Encodable[] arr = new ASN1Encodable[4];
        arr[0] = new ASN1Integer(c1x);
        arr[1] = new ASN1Integer(c1y);

        if (SM2Engine.Mode.C1C2C3 == SM2Engine.Mode.C1C3C2) {
            System.arraycopy(cipher, startPos, c2, 0, c2.length);
            startPos += c2.length;
            System.arraycopy(cipher, startPos, c3, 0, c3.length);
            arr[2] = new DEROctetString(c2);
            arr[3] = new DEROctetString(c3);

        } else if (SM2Engine.Mode.C1C3C2 == SM2Engine.Mode.C1C3C2) {
            System.arraycopy(cipher, startPos, c3, 0, c3.length);
            startPos += c3.length;
            System.arraycopy(cipher, startPos, c2, 0, c2.length);
            arr[2] = new DEROctetString(c3);
            arr[3] = new DEROctetString(c2);
        }

        DERSequence ds = new DERSequence(arr);
        return ds.getEncoded(ASN1Encoding.DER);
    }

    /**
     * decodeDERSM2Cipher
     *
     * @param derCipher 密文byte[]
     * @return 将密文按 C1C3C2 模式排列
     */
    private static byte[] decodeDERSM2Cipher(byte[] derCipher) {
        ASN1Sequence as = ASN1Sequence.getInstance(derCipher);

        //1: deal with c1x
        byte[] c1xTemp = ((ASN1Integer) as.getObjectAt(0)).getValue().toByteArray();
        byte[] c1x = c1xTemp;

        if (c1xTemp.length > 32) {
            c1x = new byte[32];
            System.arraycopy(c1xTemp, c1xTemp.length - 32, c1x, 0, 32);
        } else if (c1xTemp.length < 32) {
            c1x = new byte[32];
            byte[] temp = new byte[32 - c1xTemp.length];
            System.arraycopy(temp, 0, c1x, 0, temp.length);
            System.arraycopy(c1xTemp, 0, c1x, temp.length, c1xTemp.length);
        }

        //2: deal with c1y
        byte[] c1yTemp = ((ASN1Integer) as.getObjectAt(1)).getValue().toByteArray();
        byte[] c1y = c1yTemp;

        if (c1yTemp.length > 32) {
            c1y = new byte[32];
            System.arraycopy(c1yTemp, c1yTemp.length - 32, c1y, 0, 32);

        } else if (c1yTemp.length < 32) {
            c1y = new byte[32];
            byte[] temp = new byte[32 - c1yTemp.length];
            System.arraycopy(temp, 0, c1y, 0, temp.length);
            System.arraycopy(c1yTemp, 0, c1y, temp.length, c1yTemp.length);
        }

        //3 : deal with c2 c3
        byte[] c2 = new byte[0];
        byte[] c3 = new byte[0];


        if (SM2Engine.Mode.C1C3C2 == SM2Engine.Mode.C1C2C3) {
            c2 = ((DEROctetString) as.getObjectAt(2)).getOctets();
            c3 = ((DEROctetString) as.getObjectAt(3)).getOctets();
        } else if (SM2Engine.Mode.C1C3C2 == SM2Engine.Mode.C1C3C2) {
            c2 = ((DEROctetString) as.getObjectAt(3)).getOctets();
            c3 = ((DEROctetString) as.getObjectAt(2)).getOctets();
        }

        byte[] cipherText = new byte[1 + c1x.length + c1y.length + c2.length + c3.length];
        int pos = 0;


        cipherText[0] = 0x04;
        pos += 1;

        System.arraycopy(c1x, 0, cipherText, pos, c1x.length);
        pos += c1x.length;

        System.arraycopy(c1y, 0, cipherText, pos, c1y.length);
        pos += c1y.length;

        if (SM2Engine.Mode.C1C3C2 == SM2Engine.Mode.C1C2C3) {
            System.arraycopy(c2, 0, cipherText, pos, c2.length);
            pos += c2.length;
            System.arraycopy(c3, 0, cipherText, pos, c3.length);

        } else if (SM2Engine.Mode.C1C3C2 == SM2Engine.Mode.C1C3C2) {
            System.arraycopy(c3, 0, cipherText, pos, c3.length);
            pos += c3.length;
            System.arraycopy(c2, 0, cipherText, pos, c2.length);
        }

        return cipherText;
    }

    /**
     * 签名
     *
     * @param plainText    明文
     * @param prvKeyBase64 Base64 格式的密钥
     * @return 签名结果
     */
    public static String signBase64(byte[] plainText, String prvKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String prvKeyHex = base64ToHexKey(prvKeyBase64);
        return signHex(plainText, prvKeyHex);
    }

    /**
     * 签名
     *
     * @param plainText 明文
     * @param prvKeyHex Hex 格式的密钥
     * @return 签名结果
     */
    public static String signHex(byte[] plainText, String prvKeyHex) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        BigInteger bigInteger = new BigInteger(prvKeyHex, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger, ecParameterSpec));
        signature.initSign(privateKey);
        signature.update(plainText);
        return Base64.toBase64String(signature.sign());
    }

    /**
     * 验签
     *
     * @param plainText      明文
     * @param signatureValue 签名
     * @param pubKeyBase64   Base64格式的公钥
     * @return 验签结果
     */
    public static boolean verifyBase64(String plainText, String signatureValue, String pubKeyBase64) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
        return verify(plainText, signatureValue, Hex.decode(pubKeyBase64));
    }

    /**
     * 验签
     *
     * @param plainText      明文
     * @param signatureValue 签名
     * @param pubKeyHex      Hex 格式的公钥
     * @return 验签结果
     */
    public static boolean verifyHex(String plainText, String signatureValue, String pubKeyHex) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
        return verify(plainText, signatureValue, Base64.decode(pubKeyHex));
    }

    /**
     * 验签
     *
     * @param plainText      明文
     * @param signatureValue 签名
     * @param pubKey         Base64 或 Hex 处理后的 公钥
     * @return 验签结果
     */
    public static boolean verify(String plainText, String signatureValue, byte[] pubKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

        byte[] decodeDerByte = decodeDERSignature(Base64.decode(signatureValue));
        byte[] rmPaddingBytes = removePadding(decodeDerByte);

        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        ECPoint ecPoint = parameters.getCurve().decodePoint(pubKey);
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        signature.initVerify(key);
        signature.update(plainText.getBytes(StandardCharsets.UTF_8));
        return signature.verify(rmPaddingBytes);
    }

    /**
     * 签名处理
     *
     * @param signature 签名
     * @return 签名处理后的byte[]
     */
    private static byte[] decodeDERSignature(byte[] signature) {
        try {
            final ASN1InputStream stream = new ASN1InputStream(new ByteArrayInputStream(signature));
            final ASN1Sequence primitive = (ASN1Sequence) stream.readObject();
            final Enumeration enumeration = primitive.getObjects();

            final BigInteger R = ((ASN1Integer) enumeration.nextElement()).getValue();
            final BigInteger S = ((ASN1Integer) enumeration.nextElement()).getValue();

            final byte[] bytes = new byte[64];

            final byte[] r = format(R.toByteArray());
            final byte[] s = format(S.toByteArray());

            System.arraycopy(r, 0, bytes, 0, 32);
            System.arraycopy(s, 0, bytes, 32, 32);
            return bytes;
        } catch (Exception e) {
            throw new IllegalStateException("Decode DER signature failed", e);
        }
    }

    /**
     * format
     *
     * @param value 格式化数据
     * @return 返回格式化后的byte[]
     */
    static byte[] format(byte[] value) {
        if (value.length == 32) {
            return value;
        } else {
            final byte[] bytes = new byte[32];
            if (value.length > 32) {
                System.arraycopy(value, value.length - 32, bytes, 0, 32);
            } else {
                System.arraycopy(value, 0, bytes, 32 - value.length, value.length);
            }
            return bytes;
        }
    }

    /**
     * removePadding
     *
     * @param signedData 签名数据
     * @return 返回处理的byte[]
     */
    private static byte[] removePadding(byte[] signedData) throws IOException {

        int rSrcPos;
        int sSrcPos;

        if (signedData.length == 64) {
            rSrcPos = 0;
            sSrcPos = 32;
        } else if ((signedData[3] == 32) && signedData[4] == 0 || signedData[5 + signedData[3]] == 32 && signedData[6 + signedData[3]] == 0) {
            rSrcPos = signedData[3] - 28;
            sSrcPos = signedData[3] + signedData[5 + signedData[3]] - 26;
        } else {
            return signedData;
        }

        byte[] r = new byte[32];
        byte[] s = new byte[32];

        System.arraycopy(signedData, rSrcPos, r, 0, r.length);
        System.arraycopy(signedData, sSrcPos, s, 0, s.length);

        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(new BigInteger(1, r)));
        v.add(new ASN1Integer(new BigInteger(1, s)));

        return new DERSequence(v).getEncoded();

    }
}
