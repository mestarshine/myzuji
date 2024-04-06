package com.myzuji.sadk.signature.rsa;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.util.FileUtil;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.Signer;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.signers.RSADigestSigner;
import com.myzuji.sadk.system.Mechanisms;
import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.*;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;

public class RSASignUtil {
    public RSASignUtil() {
    }

    public static boolean verifySign(String digestAlgorithm, Key pubKey, byte[] sourceData, byte[] signature) throws PKIException {
        if (sourceData != null && signature != null) {
            Signer signer = getSigner(digestAlgorithm);
            if ("SM2".equalsIgnoreCase(pubKey.getAlgorithm())) {
                return false;
            } else {
                RSAPublicKey publicKey = (RSAPublicKey) pubKey;
                RSAKeyParameters pubParameter = generatePublicKeyParameter(publicKey);
                signer.init(false, pubParameter);

                try {
                    signer.update(sourceData, 0, sourceData.length);
                } catch (Exception var8) {
                    Exception e = var8;
                    throw new PKIException(PKIException.VERIFY_SIGN, PKIException.VERIFY_SIGN_DES, e);
                }

                return signer.verifySignature(signature);
            }
        } else {
            return false;
        }
    }

    public static boolean verifySignFile(String digestAlgorithm, Key pubKey, String sourceFilePath, byte[] signature) throws Exception {
        if (sourceFilePath != null && signature != null) {
            Signer signer = getSigner(digestAlgorithm);
            RSAPublicKey publicKey = (RSAPublicKey) pubKey;
            RSAKeyParameters pubParameter = generatePublicKeyParameter(publicKey);
            signer.init(false, pubParameter);
            BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(sourceFilePath));

            try {
                int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
                byte[] buffer = new byte[buffer_size];
                int i = bfis.read(buffer);
                if (i == -1) {
                    throw new Exception("the source data is null!");
                }

                do {
                    signer.update(buffer, 0, i);
                    i = bfis.read(buffer);
                } while (i != -1);
            } finally {
                if (bfis != null) {
                    bfis.close();
                }

            }

            return signer.verifySignature(signature);
        } else {
            return false;
        }
    }

    public static boolean verifySignFile(String digestAlgorithm, Key pubKey, String sourceFilePath, long startPos, long dataLength, byte[] signature, String generate_sourceFilePath) throws Exception {
        RandomAccessFile bfis = null;
        FileOutputStream gen_fos = null;

        boolean var23;
        try {
            if (sourceFilePath == null || signature == null) {
                boolean var22 = false;
                return var22;
            }

            Signer signer = getSigner(digestAlgorithm);
            RSAPublicKey publicKey = (RSAPublicKey) pubKey;
            RSAKeyParameters pubParameter = generatePublicKeyParameter(publicKey);
            signer.init(false, pubParameter);
            bfis = new RandomAccessFile(new File(sourceFilePath), "r");
            bfis.seek(startPos);
            if (generate_sourceFilePath != null && !generate_sourceFilePath.trim().equals("")) {
                gen_fos = new FileOutputStream(generate_sourceFilePath);
            }

            byte[] buffer;
            if (dataLength > (long) FileAndBufferConfig.BIG_FILE_BUFFER) {
                buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
                long readTotalLen = 0L;

                int readLen;
                while ((readLen = bfis.read(buffer)) > 0 && readTotalLen < dataLength) {
                    if (readTotalLen + (long) readLen > dataLength) {
                        int left = (int) (dataLength - readTotalLen);
                        signer.update(buffer, 0, left);
                        FileUtil.writeBytesToFile(buffer, 0, left, gen_fos);
                        break;
                    }

                    readTotalLen += (long) readLen;
                    signer.update(buffer, 0, readLen);
                    FileUtil.writeBytesToFile(buffer, 0, readLen, gen_fos);
                }
            } else {
                buffer = new byte[(int) dataLength];
                int i = bfis.read(buffer);
                signer.update(buffer, 0, i);
                FileUtil.writeBytesToFile(buffer, 0, i, gen_fos);
            }

            var23 = signer.verifySignature(signature);
        } finally {
            if (bfis != null) {
                bfis.close();
            }

            if (gen_fos != null) {
                gen_fos.close();
            }

        }

        return var23;
    }

    private static Signer getSigner(String digestAlgorithm) throws PKIException {
        Digest engine = Mechanisms.getDigest(digestAlgorithm);
        if (engine == null) {
            throw new PKIException("can not support this degest algorithm:" + digestAlgorithm);
        } else {
            return new RSADigestSigner(engine);
        }
    }

    private static RSAKeyParameters generatePublicKeyParameter(RSAPublicKey key) {
        return new RSAKeyParameters(false, key.getModulus(), key.getPublicExponent());
    }
}
