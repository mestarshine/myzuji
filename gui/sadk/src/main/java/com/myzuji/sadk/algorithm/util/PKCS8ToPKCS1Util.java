package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;

import java.security.Key;

public class PKCS8ToPKCS1Util {
    public PKCS8ToPKCS1Util() {
    }

    public static byte[] RSAP8ToP1PriKey(Key priKey) throws Exception {
        byte[] data = priKey.getEncoded();
        ASN1Sequence s = ASN1Sequence.getInstance(data);
        byte[] octetStr = s.getObjectAt(2).toASN1Primitive().getEncoded();
        int len = octetStr[1] & 127;
        int ret_len = octetStr.length - len - 2;
        byte[] ret = new byte[ret_len];
        System.arraycopy(octetStr, len + 2, ret, 0, ret_len);
        return ret;
    }

    public static byte[] RSAP8ToP1PubKey(Key pubKey) throws Exception {
        byte[] data = pubKey.getEncoded();
        ASN1Sequence s = ASN1Sequence.getInstance(data);
        byte[] bitStr = s.getObjectAt(1).toASN1Primitive().getEncoded();
        int len = bitStr[1] & 127;
        int ret_len = bitStr.length - len - 3;
        byte[] ret = new byte[ret_len];
        System.arraycopy(bitStr, len + 3, ret, 0, ret_len);
        return ret;
    }
}
