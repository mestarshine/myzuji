package com.myzuji.sadk.system;

import com.myzuji.sadk.asn1.DERHeader;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Cipher;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Signature;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;

public final class SM2OutputFormat {
    static boolean standard = true;

    private SM2OutputFormat() {
    }

    public static byte[] sm2FormatEncryptedRAWBytes(byte[] encryptedBytes) {
        if (encryptedBytes != null && encryptedBytes.length > 96 && DERHeader.checkedASN1Sequence(encryptedBytes)) {
            encryptedBytes = ASN1SM2Cipher.getInstance(encryptedBytes).getEncryptedBytes(4);
        }

        return encryptedBytes;
    }

    public static final byte[] sm2FormatSigned64Bytes(byte[] signedBytes) {
        if (signedBytes != null && signedBytes.length != 64 && DERHeader.checkedASN1Sequence(signedBytes)) {
            ASN1SM2Signature signval = ASN1SM2Signature.getInstance(signedBytes);
            signedBytes = new byte[64];
            System.arraycopy(BigIntegers.asUnsignedByteArray(32, signval.getR().getPositiveValue()), 0, signedBytes, 0, 32);
            System.arraycopy(BigIntegers.asUnsignedByteArray(32, signval.getS().getPositiveValue()), 0, signedBytes, 32, 32);
        }

        return signedBytes;
    }

    public static final byte[] sm2FormatSignedASN1Bytes(byte[] signedBytes) {
        if (signedBytes != null && signedBytes.length == 64) {
            try {
                signedBytes = (new ASN1SM2Signature(signedBytes)).getEncoded("DER");
            } catch (Exception var2) {
                throw new SecurityException("sm2FormatSignedASN1Bytes failure");
            }
        }

        return signedBytes;
    }

    public static final byte[] sm2FormatSignedBytes(byte[] signedBytes) {
        if (standard) {
            signedBytes = sm2FormatSignedASN1Bytes(signedBytes);
        } else {
            signedBytes = sm2FormatSigned64Bytes(signedBytes);
        }

        return signedBytes;
    }

    public static boolean isSM2EncryptedFormatRAWBytes(byte[] encryptedBytes) {
        boolean checked = false;
        if (encryptedBytes != null && encryptedBytes.length >= 96 && !DERHeader.checkedASN1Sequence(encryptedBytes)) {
            BigInteger x = BigIntegers.fromUnsignedByteArray(encryptedBytes, 0, 32);
            BigInteger y = BigIntegers.fromUnsignedByteArray(encryptedBytes, 32, 32);
            ECPoint point = SM2Params.sm2ParameterSpec.getCurve().createPoint(x, y);
            checked = point.isValid();
        }

        return checked;
    }

    public static boolean isSM2EncryptedFormatASNBytes(byte[] encryptedBytes) {
        boolean checked = false;
        if (encryptedBytes != null && encryptedBytes.length >= 96 && DERHeader.checkedASN1Sequence(encryptedBytes)) {
            try {
                ASN1SM2Cipher obj = ASN1SM2Cipher.getInstance(encryptedBytes);
                BigInteger x = obj.getXCoordinate().getPositiveValue();
                BigInteger y = obj.getYCoordinate().getPositiveValue();
                ECPoint point = SM2Params.sm2ParameterSpec.getCurve().createPoint(x, y);
                checked = point.isValid();
            } catch (Exception var6) {
                checked = false;
            }
        }

        return checked;
    }
}
