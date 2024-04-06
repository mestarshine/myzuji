package com.myzuji.sadk.org.bouncycastle.asn1.sm2;

import com.myzuji.sadk.asn1.DERHeader;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Enumeration;

public final class ASN1SM2Cipher extends ASN1Object {
    private ASN1Integer xCoordinate;
    private ASN1Integer yCoordinate;
    private ASN1OctetString hashValue;
    private ASN1OctetString cipherText;

    public static ASN1SM2Cipher getInstance(Object o) throws IllegalArgumentException {
        if (o instanceof ASN1SM2Cipher) {
            return (ASN1SM2Cipher) o;
        } else {
            return o != null ? new ASN1SM2Cipher(ASN1Sequence.getInstance(o)) : null;
        }
    }

    public ASN1SM2Cipher(ASN1Integer xCoordinate, ASN1Integer yCoordinate, ASN1OctetString hashValue, ASN1OctetString cipherText) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.hashValue = hashValue;
        this.cipherText = cipherText;
    }

    public ASN1SM2Cipher(BigInteger x, BigInteger y, ASN1OctetString hashValue, ASN1OctetString cipherText) {
        this.yCoordinate = new ASN1Integer(x);
        this.yCoordinate = new ASN1Integer(y);
        this.hashValue = hashValue;
        this.cipherText = cipherText;
    }

    public ASN1SM2Cipher(BigInteger x, BigInteger y, byte[] hashValue, byte[] cipherText) {
        this.xCoordinate = new ASN1Integer(x);
        this.yCoordinate = new ASN1Integer(y);
        this.hashValue = new DEROctetString(hashValue);
        this.cipherText = new DEROctetString(cipherText);
    }

    public ASN1SM2Cipher(byte[] x, byte[] y, byte[] hashValue, byte[] cipherText) {
        this.xCoordinate = new ASN1Integer(new BigInteger(1, x));
        this.yCoordinate = new ASN1Integer(new BigInteger(1, y));
        this.hashValue = new DEROctetString(hashValue);
        this.cipherText = new DEROctetString(cipherText);
    }

    public ASN1SM2Cipher(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        this.xCoordinate = (ASN1Integer) e.nextElement();
        this.yCoordinate = (ASN1Integer) e.nextElement();
        this.hashValue = DEROctetString.getInstance(e.nextElement());
        this.cipherText = DEROctetString.getInstance(e.nextElement());
    }

    public ASN1SM2Cipher(byte[] encryptedBytes, int encryptedType) throws SecurityException {
        if (encryptedBytes != null && encryptedBytes.length >= 96) {
            if (encryptedType == 1) {
                ASN1InputStream asn1Is = null;

                try {
                    asn1Is = new ASN1InputStream(encryptedBytes);
                    ASN1Sequence seq = (ASN1Sequence) asn1Is.readObject();
                    Enumeration e = seq.getObjects();
                    this.xCoordinate = (ASN1Integer) e.nextElement();
                    this.yCoordinate = (ASN1Integer) e.nextElement();
                    this.hashValue = DEROctetString.getInstance(e.nextElement());
                    this.cipherText = DEROctetString.getInstance(e.nextElement());
                } catch (Exception var13) {
                    throw new SecurityException("Unknown encryptedBytes: " + var13.getMessage());
                } finally {
                    if (asn1Is != null) {
                        try {
                            asn1Is.close();
                        } catch (Exception var12) {
                        }
                    }

                }

            } else {
                int offset = 0;
                switch (encryptedType) {
                    case 2:
                        ++offset;
                    case 4:
                    case 16:
                        break;
                    case 8:
                        ++offset;
                        break;
                    default:
                        throw new SecurityException("Unknown SM2EncryptedType =" + encryptedType);
                }

                if (offset != 0) {
                    if (encryptedBytes[0] != 4) {
                        throw new SecurityException("Unknown encryptedBytes[0]");
                    }

                    if (encryptedBytes.length < 97) {
                        throw new SecurityException("Unknown encryptedBytes[Length]");
                    }
                }

                byte[] Xbyte = new byte[32];
                byte[] Ybyte = new byte[32];
                byte[] C3byte = new byte[32];
                byte[] C2byte = new byte[encryptedBytes.length - 96 - offset];
                System.arraycopy(encryptedBytes, offset, Xbyte, 0, Xbyte.length);
                offset += Xbyte.length;
                System.arraycopy(encryptedBytes, offset, Ybyte, 0, Ybyte.length);
                offset += Ybyte.length;
                int var10000;
                if (encryptedType != 2 && encryptedType != 4) {
                    System.arraycopy(encryptedBytes, offset, C2byte, 0, C2byte.length);
                    offset += C2byte.length;
                    System.arraycopy(encryptedBytes, offset, C3byte, 0, C3byte.length);
                    var10000 = offset + C3byte.length;
                } else {
                    System.arraycopy(encryptedBytes, offset, C3byte, 0, C3byte.length);
                    offset += C3byte.length;
                    System.arraycopy(encryptedBytes, offset, C2byte, 0, C2byte.length);
                    var10000 = offset + C2byte.length;
                }

                this.xCoordinate = new ASN1Integer(new BigInteger(1, Xbyte));
                this.yCoordinate = new ASN1Integer(new BigInteger(1, Ybyte));
                this.hashValue = new DEROctetString(C3byte);
                this.cipherText = new DEROctetString(C2byte);
            }
        } else {
            throw new SecurityException("Unknown encryptedBytes");
        }
    }

    public ASN1Integer getXCoordinate() {
        return this.xCoordinate;
    }

    public ASN1Integer getYCoordinate() {
        return this.yCoordinate;
    }

    public ASN1OctetString getHashValue() {
        return this.hashValue;
    }

    public ASN1OctetString getCipherText() {
        return this.cipherText;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(this.xCoordinate);
        v.add(this.yCoordinate);
        v.add(this.hashValue);
        v.add(this.cipherText);
        return new DERSequence(v);
    }

    public byte[] getEncryptedBytes(int encryptedType) throws SecurityException {
        if (this.xCoordinate == null) {
            throw new SecurityException("xCoordinate unknown");
        } else if (this.yCoordinate == null) {
            throw new SecurityException("yCoordinate unknown");
        } else if (this.cipherText == null) {
            throw new SecurityException("cipherText unknown");
        } else if (this.hashValue == null) {
            throw new SecurityException("hashValue unknown");
        } else {
            byte[] resultBytes = null;

            try {
                byte[] Xbytes = BigIntegers.asUnsignedByteArray(32, this.xCoordinate.getValue());
                byte[] Ybytes = BigIntegers.asUnsignedByteArray(32, this.yCoordinate.getValue());
                byte[] C2Bytes = this.cipherText.getOctets();
                byte[] C3Bytes = this.hashValue.getOctets();
                if (encryptedType == 0) {
                    resultBytes = this.getEncoded();
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (encryptedType == 2 || encryptedType == 8) {
                        baos.write(4);
                    }

                    baos.write(Xbytes);
                    baos.write(Ybytes);
                    if (encryptedType != 2 && encryptedType != 4) {
                        baos.write(C2Bytes);
                        baos.write(C3Bytes);
                    } else {
                        baos.write(C3Bytes);
                        baos.write(C2Bytes);
                    }

                    Xbytes = null;
                    Ybytes = null;
                    C2Bytes = null;
                    C3Bytes = null;
                    resultBytes = baos.toByteArray();
                }
            } catch (Exception e) {
                throw new SecurityException("getEncryptedBytes Failure: " + e.getMessage());
            }

            if (resultBytes == null) {
                throw new SecurityException("getEncryptedBytes Failure: unknown SM2EncryptedType =" + encryptedType);
            } else {
                return resultBytes;
            }
        }
    }

    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("SM2Cipher [xCoordinate=");
        builder.append(this.xCoordinate);
        builder.append(", yCoordinate=");
        builder.append(this.yCoordinate);
        builder.append(", hashValue=");
        builder.append(this.hashValue);
        builder.append(", cipherText=");
        builder.append(this.cipherText);
        builder.append("]");
        return builder.toString();
    }

    public static final boolean isASN1Type(byte[] encoding) {
        if (encoding != null && encoding.length >= 96) {
            try {
                return DERHeader.checkedASN1Sequence(encoding);
            } catch (Exception var2) {
                return false;
            }
        } else {
            return false;
        }
    }

    public interface SM2EncryptedType {
        int C1_C3_C2_WITH_ASN1 = 1;
        int C1_C3_C2_WITH_0x04 = 2;
        int C1_C3_C2_WITHOUT_0x04 = 4;
        int C1_C2_C3_WITH_0x04 = 8;
        int C1_C2_C3_WITHOUT_0x04 = 16;
    }
}
