package com.myzuji.sadk.signature.sm2;

import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.util.FileUtil;
import com.myzuji.sadk.lib.crypto.jni.JNIDigest;
import com.myzuji.sadk.lib.crypto.jni.JNISoftLib;
import com.myzuji.sadk.lib.crypto.jni.Session;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.security.PublicKey;

public class SM2HashUtil {
    public SM2HashUtil() {
    }

    public static byte[] hashFile(boolean ifZValue, byte[] userId, String sourceFilePath, long startPos, long dataLength, PublicKey pubKey, String generate_sourceFilePath, Session session) throws Exception {
        RandomAccessFile bfis = null;
        FileOutputStream gen_fos = null;

        byte[] z;
        try {
            byte[] out = new byte[32];
            bfis = new RandomAccessFile(new File(sourceFilePath), "r");
            if (generate_sourceFilePath != null && !generate_sourceFilePath.trim().equals("")) {
                gen_fos = new FileOutputStream(generate_sourceFilePath);
            }

            bfis.seek(startPos);
            int buffer_size = FileAndBufferConfig.BIG_FILE_BUFFER;
            byte[] buffer = new byte[buffer_size];
            long readTotalLen = 0L;
            SM3Digest digest = null;
            JNIDigest sm3_jni = null;
            if (session instanceof JNISoftLib) {
                sm3_jni = new JNIDigest();
                sm3_jni.init(922);
            } else {
                digest = new SM3Digest();
            }

            if (ifZValue) {
                z = ((SM2PublicKey) pubKey).getDefaultZ();
                update(sm3_jni, digest, z, 0, z.length, false, (byte[]) null, session);
            }

            int readLen;
            if (dataLength > (long) FileAndBufferConfig.BIG_FILE_BUFFER) {
                while ((readLen = bfis.read(buffer)) > 0 && readTotalLen < dataLength) {
                    if (readTotalLen + (long) readLen > dataLength) {
                        int left = (int) (dataLength - readTotalLen);
                        update(sm3_jni, digest, buffer, 0, left, false, (byte[]) null, session);
                        FileUtil.writeBytesToFile(buffer, 0, left, gen_fos);
                        break;
                    }

                    readTotalLen += (long) readLen;
                    update(sm3_jni, digest, buffer, 0, readLen, false, (byte[]) null, session);
                    FileUtil.writeBytesToFile(buffer, 0, readLen, gen_fos);
                }
            } else {
                buffer = new byte[(int) dataLength];
                readLen = bfis.read(buffer);
                update(sm3_jni, digest, buffer, 0, readLen, false, (byte[]) null, session);
                FileUtil.writeBytesToFile(buffer, 0, readLen, gen_fos);
            }

            update(sm3_jni, digest, buffer, 0, 0, true, out, session);
            z = out;
        } finally {
            if (bfis != null) {
                bfis.close();
            }

            if (gen_fos != null) {
                gen_fos.close();
            }

        }

        return z;
    }

    private static void update(JNIDigest sm3_jni, SM3Digest digest, byte[] data, int pos, int len, boolean dofinal, byte[] out, Session session) throws Exception {
        if (session instanceof JNISoftLib) {
            if (dofinal) {
                sm3_jni.doFinal(out);
            } else {
                byte[] temp = new byte[len];
                System.arraycopy(data, 0, temp, 0, len);
                sm3_jni.update(temp);
            }
        } else if (dofinal) {
            digest.doFinal(out, 0);
        } else {
            digest.update(data, pos, len);
        }

    }
}
