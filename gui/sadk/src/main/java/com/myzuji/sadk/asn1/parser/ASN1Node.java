package com.myzuji.sadk.asn1.parser;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ASN1Node {
    public File f;
    public int tag;
    public int valueLengthSize;
    public long valueLength;
    public long valueStartPos;
    public long totalLength;
    public boolean isInfiniteLength;
    public boolean berNode;
    public ASN1Node parentNode;
    public ArrayList childNodes = new ArrayList();

    public ASN1Node() {
    }

    public byte[] getData() throws Exception {
        RandomAccessFile rs = null;

        byte[] var3;
        try {
            rs = new RandomAccessFile(this.f, "r");
            byte[] usefulData = new byte[(int) this.totalLength];
            if (this.berNode) {
                rs.seek(this.valueStartPos - this.totalLength + this.valueLength + 2L);
            } else {
                rs.seek(this.valueStartPos - this.totalLength + this.valueLength);
            }

            rs.read(usefulData);
            rs.close();
            var3 = usefulData;
        } catch (Exception var7) {
            Exception e = var7;
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }

        }

        return var3;
    }
}
