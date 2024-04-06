package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.CBCParam;
import com.myzuji.sadk.algorithm.common.Mechanism;
import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM4Engine;
import com.myzuji.sadk.asn1.parser.ASN1Node;
import com.myzuji.sadk.lib.crypto.jni.JNISymAlg;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1OutputStream;
import com.myzuji.sadk.org.bouncycastle.crypto.BlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.StreamCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.DESedeEngine;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RC4Engine;
import com.myzuji.sadk.org.bouncycastle.crypto.modes.CBCBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PKCS7Padding;
import com.myzuji.sadk.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.myzuji.sadk.org.bouncycastle.crypto.params.KeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithIV;
import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.*;

public class BigFileCipherUtil {
    public BigFileCipherUtil() {
    }

    public static void bigFileBlockCipher(boolean encryptFlag, Mechanism alg, byte[] key, InputStream inputStream, OutputStream outputStream) throws Exception {
        try {
            String type = alg.getMechanismType();
            PaddedBufferedBlockCipher cipher = null;
            RC4Engine rc4 = null;
            if (type.equals("RC4")) {
                rc4 = new RC4Engine();
                KeyParameter params = new KeyParameter(key);
                rc4.init(encryptFlag, params);
            } else {
                CBCParam param;
                if (type.equals("DESede/CBC/PKCS7Padding")) {
                    cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()), new PKCS7Padding());
                    param = (CBCParam) alg.getParam();
                    ParametersWithIV params = new ParametersWithIV(new KeyParameter(key), param.getIv());
                    cipher.init(encryptFlag, params);
                } else if (type.equals("SM4/CBC/PKCS7Padding")) {
                    cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new SM4Engine()), new PKCS7Padding());
                    param = (CBCParam) alg.getParam();
                    ParametersWithIV params = new ParametersWithIV(new KeyParameter(key), param.getIv());
                    cipher.init(encryptFlag, params);
                } else if (type.equals("DESede/ECB/PKCS7Padding")) {
                    cipher = new PaddedBufferedBlockCipher(new DESedeEngine(), new PKCS7Padding());
                    KeyParameter params = new KeyParameter(key);
                    cipher.init(encryptFlag, params);
                } else {
                    if (!type.equals("SM4/ECB/PKCS7Padding")) {
                        throw new PKIException("do not support this algorithm:" + type);
                    }

                    cipher = new PaddedBufferedBlockCipher(new SM4Engine(), new PKCS7Padding());
                    KeyParameter params = new KeyParameter(key);
                    cipher.init(encryptFlag, params);
                }
            }

            int readLen = 1;
            byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes;
            if (rc4 != null) {
                outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];

                while ((readLen = inputStream.read(buffer)) > 0) {
                    rc4.processBytes(buffer, 0, readLen, outBytes, 0);
                    outputStream.write(outBytes, 0, readLen);
                }
            } else {
                outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
                int processLen = 1;
                boolean isFinal = false;

                int len;
                while ((readLen = inputStream.read(buffer)) > 0) {
                    if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER) {
                        len = cipher.processBytes(buffer, 0, readLen, outBytes, 0);
                        int len2 = cipher.doFinal(outBytes, len);
                        outputStream.write(outBytes, 0, len + len2);
                        isFinal = true;
                        break;
                    }

                    processLen = cipher.processBytes(buffer, 0, readLen, outBytes, 0);
                    outputStream.write(outBytes, 0, processLen);
                }

                if (!isFinal) {
                    len = cipher.doFinal(outBytes, 0);
                    outputStream.write(outBytes, 0, len);
                }
            }
        } catch (Exception var18) {
            Exception e = var18;
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

        }

    }

    public static void bigFileDecrypt_JNI(int nid_type, byte[] key, CBCParam param, ASN1Node file_encrypted, OutputStream fos) throws Exception {
        RandomAccessFile raf = null;

        try {
            JNISymAlg jni = new JNISymAlg();
            if (param == null) {
                jni.decryptInit(nid_type, key, (byte[]) null);
            } else {
                jni.decryptInit(nid_type, key, param.getIv());
            }

            File encrypted_f = file_encrypted.f;
            long valueStartPos = file_encrypted.valueStartPos;
            long totalLength = file_encrypted.valueLength;
            long readTotalLen = 0L;
            int readLen = 1;
            int processLen = 1;
            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
            raf = new RandomAccessFile(encrypted_f, "r");
            raf.seek(valueStartPos);
            int len1;
            int len2;
            if (totalLength <= (long) FileAndBufferConfig.BIG_FILE_BUFFER) {
                readLen = raf.read(temp, 0, (int) totalLength);
                len1 = jni.decryptProcess(temp, 0, readLen, outBytes, 0);
                len2 = jni.decryptFinal(outBytes, len1);
                fos.write(outBytes, 0, len1 + len2);
            } else {
                while ((readLen = raf.read(temp)) > 0) {
                    readTotalLen += (long) readLen;
                    if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER || readTotalLen >= totalLength) {
                        len1 = jni.decryptProcess(temp, 0, readLen, outBytes, 0);
                        len2 = jni.decryptFinal(outBytes, len1);
                        fos.write(outBytes, 0, len1 + len2);
                        break;
                    }

                    processLen = jni.decryptProcess(temp, 0, readLen, outBytes, 0);
                    fos.write(outBytes, 0, processLen);
                }
            }
        } catch (Exception var23) {
            Exception e = var23;
            throw e;
        } finally {
            if (raf != null) {
                raf.close();
            }

            if (fos != null) {
                fos.close();
            }

        }

    }

    public static void bigFileDecrypt_JNI(int nid_type, byte[] key, CBCParam param, ASN1Node file_encrypted, OutputStream fos, RandomAccessFile raf) throws Exception {
        try {
            JNISymAlg jni = new JNISymAlg();
            if (param == null) {
                jni.decryptInit(nid_type, key, (byte[]) null);
            } else {
                jni.decryptInit(nid_type, key, param.getIv());
            }

            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
            long valueStartPos = 0L;
            int valueLen = 1;
            int childLen = file_encrypted.childNodes.size();
            int readTotalLen = 0;
            int len1 = 0;

            int len2;
            for (len2 = 0; len2 < childLen; ++len2) {
                ASN1Node file_encrypted_child = (ASN1Node) file_encrypted.childNodes.get(len2);
                valueStartPos = file_encrypted_child.valueStartPos;
                valueLen = (int) file_encrypted_child.valueLength;
                raf.seek(valueStartPos);
                int currentLen = raf.read(temp, readTotalLen, valueLen);
                readTotalLen += currentLen;
                if (readTotalLen >= FileAndBufferConfig.BIG_FILE_BUFFER) {
                    len1 = jni.decryptProcess(temp, 0, readTotalLen, outBytes, 0);
                    fos.write(outBytes, 0, len1);
                    readTotalLen = 0;
                }
            }

            if (readTotalLen > 0) {
                len1 = jni.decryptProcess(temp, 0, readTotalLen, outBytes, 0);
            }

            len2 = jni.encryptFinal(outBytes, len1);
            fos.write(outBytes, 0, len1 + len2);
        } catch (Exception var21) {
            Exception e = var21;
            throw e;
        } finally {
            if (raf != null) {
                raf.close();
            }

            if (fos != null) {
                fos.close();
            }

        }
    }

    public static void bigFileBlockDecrypt(byte[] key, BlockCipher engine, CBCParam param, ASN1Node file_encrypted, OutputStream fos) throws Exception {
        RandomAccessFile raf = null;

        try {
            Exception e;
            try {
                e = null;
                PaddedBufferedBlockCipher cipher;
                if (param == null) {
                    cipher = new PaddedBufferedBlockCipher(engine, new PKCS7Padding());
                    KeyParameter params = new KeyParameter(key);
                    cipher.init(false, params);
                } else {
                    cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine), new PKCS7Padding());
                    ParametersWithIV params = new ParametersWithIV(new KeyParameter(key), param.getIv());
                    cipher.init(false, params);
                }

                File encrypted_f = file_encrypted.f;
                long valueStartPos = file_encrypted.valueStartPos;
                long totalLength = file_encrypted.valueLength;
                long readTotalLen = 0L;
                int readLen = 1;
                int processLen = 1;
                byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
                byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
                raf = new RandomAccessFile(encrypted_f, "r");
                raf.seek(valueStartPos);
                int len1;
                int len2;
                if (totalLength <= (long) FileAndBufferConfig.BIG_FILE_BUFFER) {
                    readLen = raf.read(temp, 0, (int) totalLength);
                    len1 = cipher.processBytes(temp, 0, readLen, outBytes, 0);
                    len2 = cipher.doFinal(outBytes, len1);
                    fos.write(outBytes, 0, len1 + len2);
                } else {
                    while ((readLen = raf.read(temp)) > 0) {
                        readTotalLen += (long) readLen;
                        if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER || readTotalLen >= totalLength) {
                            len1 = cipher.processBytes(temp, 0, readLen, outBytes, 0);
                            len2 = cipher.doFinal(outBytes, len1);
                            fos.write(outBytes, 0, len1 + len2);
                            break;
                        }

                        processLen = cipher.processBytes(temp, 0, readLen, outBytes, 0);
                        fos.write(outBytes, 0, processLen);
                    }
                }
            } catch (Exception var23) {
                e = var23;
                throw e;
            }
        } finally {
            if (raf != null) {
                raf.close();
            }

            if (fos != null) {
                fos.close();
            }

        }

    }

    public static void bigFileBlockDecrypt(PaddedBufferedBlockCipher cipher, ASN1Node file_encrypted, OutputStream fos, RandomAccessFile raf) throws Exception {
        try {
            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
            long valueStartPos = 0L;
            int valueLen = 1;
            int childLen = file_encrypted.childNodes.size();
            int readTotalLen = 0;
            int len1 = 0;

            int len2;
            for (len2 = 0; len2 < childLen; ++len2) {
                ASN1Node file_encrypted_child = (ASN1Node) file_encrypted.childNodes.get(len2);
                valueStartPos = file_encrypted_child.valueStartPos;
                valueLen = (int) file_encrypted_child.valueLength;
                raf.seek(valueStartPos);
                int currentLen = raf.read(temp, readTotalLen, valueLen);
                readTotalLen += currentLen;
                if (readTotalLen >= FileAndBufferConfig.BIG_FILE_BUFFER) {
                    len1 = cipher.processBytes(temp, 0, readTotalLen, outBytes, 0);
                    fos.write(outBytes, 0, len1);
                    readTotalLen = 0;
                }
            }

            if (readTotalLen > 0) {
                len1 = cipher.processBytes(temp, 0, readTotalLen, outBytes, 0);
            }

            len2 = cipher.doFinal(outBytes, len1);
            fos.write(outBytes, 0, len1 + len2);
        } catch (Exception var15) {
            Exception e = var15;
            throw e;
        }
    }

    public static void bigFileRC4Decrypt(StreamCipher cipher, byte[] key, ASN1Node file_encrypted, OutputStream fos) throws Exception {
        RandomAccessFile raf = null;

        try {
            KeyParameter param = new KeyParameter(key);
            cipher.init(false, param);
            File encrypted_f = file_encrypted.f;
            long valueStartPos = file_encrypted.valueStartPos;
            long totalLength = file_encrypted.valueLength;
            long readTotalLen = 0L;
            int readLen = 1;
            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            raf = new RandomAccessFile(encrypted_f, "r");
            raf.seek(valueStartPos);
            if (totalLength <= (long) FileAndBufferConfig.BIG_FILE_BUFFER) {
                readLen = raf.read(temp, 0, (int) totalLength);
                cipher.processBytes(temp, 0, readLen, outBytes, 0);
                fos.write(outBytes, 0, readLen);
            } else {
                while ((readLen = raf.read(temp)) > 0) {
                    if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER || readTotalLen >= totalLength) {
                        cipher.processBytes(temp, 0, readLen, outBytes, 0);
                        fos.write(outBytes, 0, readLen);
                        break;
                    }

                    readTotalLen += (long) readLen;
                    cipher.processBytes(temp, 0, readLen, outBytes, 0);
                    fos.write(outBytes);
                }
            }
        } catch (Exception var19) {
            Exception e = var19;
            throw e;
        } finally {
            if (raf != null) {
                raf.close();
            }

        }

    }

    public static void bigFileRC4Decrypt(StreamCipher rc4, ASN1Node file_encrypted, OutputStream fos, RandomAccessFile raf) throws Exception {
        try {
            long valueStartPos = 0L;
            int readTotalLen = 0;
            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            int childLen = file_encrypted.childNodes.size();
            int valueLen = 1;

            for (int i = 0; i < childLen; ++i) {
                ASN1Node file_encrypted_child = (ASN1Node) file_encrypted.childNodes.get(i);
                valueStartPos = file_encrypted_child.valueStartPos;
                valueLen = (int) file_encrypted_child.valueLength;
                raf.seek(valueStartPos);
                int currentReadLen = raf.read(temp, readTotalLen, valueLen);
                readTotalLen += currentReadLen;
                if (readTotalLen >= FileAndBufferConfig.BIG_FILE_BUFFER) {
                    rc4.processBytes(temp, 0, readTotalLen, outBytes, 0);
                    fos.write(outBytes, 0, readTotalLen);
                    readTotalLen = 0;
                }
            }

            if (readTotalLen > 0) {
                rc4.processBytes(temp, 0, readTotalLen, outBytes, 0);
                fos.write(outBytes, 0, readTotalLen);
            }

        } catch (Exception var14) {
            Exception e = var14;
            throw e;
        }
    }

    public static void bigFileBlockEncrypt(byte[] key, BlockCipher engine, CBCParam param, File f, ASN1OutputStream out) throws Exception {
        FileInputStream fis = null;

        try {
            Exception e;
            try {
                e = null;
                PaddedBufferedBlockCipher cipher;
                if (param == null) {
                    cipher = new PaddedBufferedBlockCipher(engine, new PKCS7Padding());
                    KeyParameter params = new KeyParameter(key);
                    cipher.init(true, params);
                } else {
                    cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine), new PKCS7Padding());
                    ParametersWithIV params = new ParametersWithIV(new KeyParameter(key), param.getIv());
                    cipher.init(true, params);
                }

                int readLen = 1;
                byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
                byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
                long readTotalLen = 0L;
                long fileLen = f.length();
                int processLen = 1;
                fis = new FileInputStream(f);

                while ((readLen = fis.read(buffer)) > 0) {
                    readTotalLen += (long) readLen;
                    if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER || readTotalLen >= fileLen) {
                        int len1 = cipher.processBytes(buffer, 0, readLen, outBytes, 0);
                        int len2 = cipher.doFinal(outBytes, len1);
                        out.write(outBytes, 0, len1 + len2);
                        break;
                    }

                    processLen = cipher.processBytes(buffer, 0, readLen, outBytes, 0);
                    out.write(outBytes, 0, processLen);
                }
            } catch (Exception var20) {
                e = var20;
                throw e;
            }
        } finally {
            if (fis != null) {
                fis.close();
            }

        }

    }

    public static void bigFileRC4Encrypt(byte[] key, StreamCipher rc4, File f, ASN1OutputStream out) throws Exception {
        FileInputStream fis = null;

        try {
            KeyParameter param = new KeyParameter(key);
            rc4.init(true, param);
            int readLen = 1;
            byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
            fis = new FileInputStream(f);

            while ((readLen = fis.read(buffer)) > 0) {
                rc4.processBytes(buffer, 0, readLen, outBytes, 0);
                out.write(outBytes, 0, readLen);
            }
        } catch (Exception var12) {
            Exception e = var12;
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }

        }

    }

    public static void bigFileEncrypt_JNI(int nid_type, byte[] key, byte[] iv, File f, ASN1OutputStream out) throws Exception {
        JNISymAlg jni = new JNISymAlg();
        jni.encryptInit(nid_type, key, iv);
        int readLen = 1;
        byte[] buffer = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];
        byte[] outBytes = new byte[FileAndBufferConfig.BIG_FILE_BUFFER + 100];
        long readTotalLen = 0L;
        long fileLen = f.length();
        int processLen = 1;
        InputStream fis = null;

        try {
            fis = new FileInputStream(f);

            while ((readLen = fis.read(buffer)) > 0) {
                readTotalLen += (long) readLen;
                if (readLen != FileAndBufferConfig.BIG_FILE_BUFFER || readTotalLen >= fileLen) {
                    int len1 = jni.encryptProcess(buffer, 0, readLen, outBytes, 0);
                    int len2 = jni.encryptFinal(outBytes, len1);
                    out.write(outBytes, 0, len1 + len2);
                    break;
                }

                processLen = jni.encryptProcess(buffer, 0, readLen, outBytes, 0);
                out.write(outBytes, 0, processLen);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }

        }

    }
}
