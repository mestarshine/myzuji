package com.myzuji.sadk.algorithm.sm2;

import com.myzuji.sadk.algorithm.common.GMObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X962Parameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECDomainParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.jce.interfaces.ECPrivateKey;
import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.math.ec.FixedPointCombMultiplier;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public final class SM2PrivateKey implements ECPrivateKey {
    private static final long serialVersionUID = -3613783346296166756L;
    private final ECParameterSpec sm2ParameterSpec;
    private final BigInteger dInt;
    private final byte[] dBytes;
    private final byte[] zvalue;
    private SM2PublicKey pubKey;

    public static SM2PrivateKey getInstance(byte[] encoded) {
        return new SM2PrivateKey(encoded);
    }

    public SM2PrivateKey(byte[] encoding) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.pubKey = null;
        if (encoding == null) {
            throw new IllegalArgumentException("null not allowed for encoded");
        } else {
            switch (encoding.length) {
                case 32:
                    this.dInt = this.dIntegerFrom(encoding);
                    break;
                case 96:
                    this.dInt = this.dIntegerFrom(encoding);
                    byte[] bPubX = new byte[32];
                    byte[] bPubY = new byte[32];
                    System.arraycopy(encoding, 32, bPubX, 0, 32);
                    System.arraycopy(encoding, 64, bPubY, 0, 32);
                    this.pubKey = new SM2PublicKey(bPubX, bPubY);
                    break;
                default:
                    this.dInt = this.dIntegerFrom(encoding);
            }

            if (this.pubKey == null) {
                this.pubKey = buildPublicKey(this.dInt);
            }

            this.dBytes = BigIntegers.asUnsignedByteArray(32, this.dInt);
            this.zvalue = this.pubKey.getZvalue();
        }
    }

    public SM2PrivateKey(byte[] dBytes, byte[] bPubX, byte[] bPubY) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.pubKey = null;
        if (dBytes == null) {
            throw new IllegalArgumentException("null not allowed for dBytes");
        } else {
            this.dInt = new BigInteger(1, dBytes);
            this.dBytes = dBytes;
            if (bPubX != null && bPubY != null) {
                this.pubKey = new SM2PublicKey(bPubX, bPubY);
            } else {
                this.pubKey = buildPublicKey(this.dInt);
            }

            this.zvalue = this.pubKey.getZvalue();
        }
    }

    public SM2PrivateKey(BigInteger d, BigInteger iPubX, BigInteger iPubY) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.pubKey = null;
        if (d == null) {
            throw new IllegalArgumentException("null not allowed for d");
        } else {
            this.dInt = d;
            this.dBytes = BigIntegers.asUnsignedByteArray(32, d);
            if (iPubX != null && iPubY != null) {
                this.pubKey = new SM2PublicKey(iPubX, iPubY);
            } else {
                this.pubKey = buildPublicKey(d);
            }

            this.zvalue = this.pubKey.getZvalue();
        }
    }

    public SM2PrivateKey(ECPrivateKeyParameters ecPrivateKeyParameters) {
        this(ecPrivateKeyParameters, (ECPublicKeyParameters) null);
    }

    public SM2PrivateKey(ECPrivateKeyParameters ecPrivateKeyParameters, ECPublicKeyParameters ecPublicKeyParameters) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.pubKey = null;
        if (ecPrivateKeyParameters == null) {
            throw new IllegalArgumentException("null not allowed for ecPrivateKeyParameters");
        } else if (!SM2Params.sm2DomainParameters.equals(ecPrivateKeyParameters.getParameters())) {
            throw new IllegalArgumentException("domainParameters not allowed for ecPrivateKeyParameters");
        } else {
            this.dInt = ecPrivateKeyParameters.getD();
            this.dBytes = BigIntegers.asUnsignedByteArray(32, this.dInt);
            if (ecPublicKeyParameters == null) {
                this.pubKey = buildPublicKey(this.dInt);
            } else {
                if (!SM2Params.sm2DomainParameters.equals(ecPublicKeyParameters.getParameters())) {
                    throw new IllegalArgumentException("domainParameters not allowed for ecPublicKeyParameters");
                }

                this.pubKey = new SM2PublicKey(ecPublicKeyParameters);
            }

            this.zvalue = this.pubKey.getZvalue();
        }
    }

    private final BigInteger dIntegerFrom(byte[] encoding) {
        BigInteger d = null;
        if (encoding.length != 32 && encoding.length != 96) {
            try {
                ASN1Sequence seq = ASN1Sequence.getInstance(encoding);
                if (seq.size() < 3) {
                    throw new SecurityException("encoding not valid");
                }

                AlgorithmIdentifier aid = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
                if (!X9ObjectIdentifiers.id_ecPublicKey.equals(aid.getAlgorithm())) {
                    throw new SecurityException("encoding algorithm not valid");
                }

                ASN1Encodable param = aid.getParameters();
                if (param != null) {
                    byte[] value = param.toASN1Primitive().getEncoded();
                    if (!Arrays.equals(value, DERNull.INSTANCE.getEncoded()) && !Arrays.equals(value, GMObjectIdentifiers.sm2.getEncoded())) {
                        throw new SecurityException("encoding parameters not valid");
                    }
                }

                ASN1Encodable context = seq.getObjectAt(2);
                if (!(context instanceof DEROctetString)) {
                    throw new RuntimeException("encoding context type not valid");
                }

                d = this.dIntegerFrom((DEROctetString) context);
            } catch (Exception var7) {
                Exception e = var7;
                throw new RuntimeException("encoding not valid", e);
            }
        } else {
            byte[] value = new byte[32];
            System.arraycopy(encoding, 0, value, 0, 32);
            d = new BigInteger(1, value);
        }

        return d;
    }

    private final BigInteger dIntegerFrom(DEROctetString encoding) {
        BigInteger d = null;
        ASN1Sequence seq = ASN1Sequence.getInstance(encoding.getOctets());
        if (seq.size() < 2) {
            throw new SecurityException("encoding context not valid");
        } else {
            ASN1Encodable value = seq.getObjectAt(1);
            if (value instanceof ASN1Integer) {
                d = ((ASN1Integer) value).getPositiveValue();
            } else {
                if (!(value instanceof ASN1OctetString)) {
                    throw new SecurityException("encoding context#d not valid");
                }

                d = new BigInteger(1, ((ASN1OctetString) value).getOctets());
            }

            try {
                int i = 2;

                for (int size = seq.size(); i < size; ++i) {
                    value = seq.getObjectAt(i);
                    if (value instanceof ASN1TaggedObject) {
                        ASN1TaggedObject tag = (ASN1TaggedObject) value;
                        if (tag.getTagNo() == 1) {
                            DERBitString k = DERBitString.getInstance(tag.getObject());
                            this.pubKey = new SM2PublicKey(k.getBytes());
                        }
                    }
                }
            } catch (Exception var9) {
            }

            return d;
        }
    }

    public byte[] getEncoded() {
        IOException e;
        try {
            e = null;
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1Integer(1L));
            v.add(new ASN1Integer(this.dInt));
            if (this.pubKey != null) {
                byte[] encoding = new byte[65];
                encoding[0] = 4;
                System.arraycopy(this.pubKey.getPubX(), 0, encoding, 1, 32);
                System.arraycopy(this.pubKey.getPubY(), 0, encoding, 33, 32);
                v.add(new DERTaggedObject(true, 1, new DERBitString(encoding)));
            }

            DERSequence value = new DERSequence(v);
            v = new ASN1EncodableVector();
            v.add(new ASN1Integer(0L));
            v.add(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, GMObjectIdentifiers.sm2));
            v.add(new DEROctetString(value));
            return (new DERSequence(v)).getEncoded();
        } catch (IOException var4) {
            e = var4;
            throw new RuntimeException("SM2PrivateKey encoding failure ", e);
        }
    }

    public byte[] getEncoding() {
        try {
            X962Parameters params = new X962Parameters(DERNull.INSTANCE);
            com.myzuji.sadk.org.bouncycastle.asn1.sec.ECPrivateKey keyStructure = new com.myzuji.sadk.org.bouncycastle.asn1.sec.ECPrivateKey(this.getDByInt(), params);
            PrivateKeyInfo info = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, params), keyStructure);
            return info.getEncoded("DER");
        } catch (Exception var4) {
            Exception e = var4;
            throw new RuntimeException("SM2PrivateKey encoding failure ", e);
        }
    }


    public void setSM2PublicKey(SM2PublicKey pubKey) {
    }

    public SM2PublicKey getSM2PublicKey() {
        return this.pubKey;
    }

    public BigInteger getD() {
        return this.dInt;
    }

    public BigInteger getDByInt() {
        return this.dInt;
    }

    public byte[] getD_Bytes() {
        return (byte[]) ((byte[]) this.dBytes.clone());
    }

    public byte[] getDByBytes() {
        return (byte[]) ((byte[]) this.dBytes.clone());
    }

    public byte[] getDByBytesWithPublicKey() {
        byte[] encoded = null;
        if (this.pubKey != null) {
            encoded = new byte[96];
            System.arraycopy(this.getDByBytes(), 0, encoded, 0, 32);
            System.arraycopy(this.pubKey.getPubXByBytes(), 0, encoded, 32, 32);
            System.arraycopy(this.pubKey.getPubYByBytes(), 0, encoded, 64, 32);
        } else {
            encoded = new byte[32];
            System.arraycopy(this.getDByBytes(), 0, encoded, 0, 32);
        }

        return encoded;
    }

    public byte[] getZvalue() {
        return (byte[]) ((byte[]) this.zvalue.clone());
    }

    public ECParameterSpec getParams() {
        return this.sm2ParameterSpec;
    }

    public ECParameterSpec getParameters() {
        return this.sm2ParameterSpec;
    }

    public String getAlgorithm() {
        return "SM2";
    }

    public String getFormat() {
        return "X.509";
    }

    private static SM2PublicKey buildPublicKey(BigInteger d) {
        ECDomainParameters params = SM2Params.sm2DomainParameters;
        FixedPointCombMultiplier multiplier = new FixedPointCombMultiplier();
        ECPoint Q = multiplier.multiply(params.getG(), d).normalize();
        return new SM2PublicKey(new ECPublicKeyParameters(Q, params));
    }

    public int hashCode() {
        int prime = 0;
        int result = 1;
        result = 31 * result + (this.dInt == null ? 0 : this.dInt.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            SM2PrivateKey other = (SM2PrivateKey) obj;
            if (this.dInt == null) {
                if (other.dInt != null) {
                    return false;
                }
            } else if (!this.dInt.equals(other.dInt)) {
                return false;
            }

            return true;
        }
    }

    public String toString() {
        String separator = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        buffer.append("SM2 Private Key: ");
        buffer.append(separator);
        buffer.append("D:");
        if (this.dInt != null) {
            buffer.append(this.dInt.toString(16));
        }

        if (this.pubKey != null) {
            buffer.append(separator);
            buffer.append(this.pubKey);
        }

        return buffer.toString();
    }

    public final BigInteger dBigInteger() {
        return this.dInt;
    }

    public static void main(String[] args) {
    }
}
