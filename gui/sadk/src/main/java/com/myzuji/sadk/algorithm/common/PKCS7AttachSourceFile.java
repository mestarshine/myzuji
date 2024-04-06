package com.myzuji.sadk.algorithm.common;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1OutputStream;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Primitive;
import com.myzuji.sadk.org.bouncycastle.asn1.StreamUtil;
import com.myzuji.sadk.system.global.FileAndBufferConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PKCS7AttachSourceFile extends ASN1Primitive {
    private File f;
    private long totalLength;

    public PKCS7AttachSourceFile() {
    }

    public static PKCS7AttachSourceFile getInstance(Object obj) {
        return obj != null && obj instanceof PKCS7AttachSourceFile ? (PKCS7AttachSourceFile) obj : null;
    }

    public PKCS7AttachSourceFile(File f) {
        this.f = f;
        this.totalLength = f.length();
    }

    public int hashCode() {
        return 0;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() throws IOException {
        return 1 + StreamUtil.calculateBodyLength((int) this.totalLength) + (int) this.totalLength;
    }

    public void encode(ASN1OutputStream out) {
        try {
            out.write(4);
            out.writeLength((int) this.totalLength);
            FileInputStream fis = new FileInputStream(this.f);
            byte[] temp = new byte[FileAndBufferConfig.BIG_FILE_BUFFER];

            for (int len = fis.read(temp); len != -1; len = fis.read(temp)) {
                out.write(temp, 0, len);
            }

            fis.close();
        } catch (Exception var5) {
        }

    }

    public boolean asn1Equals(ASN1Primitive o) {
        return false;
    }
}
