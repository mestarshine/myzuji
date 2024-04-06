package com.myzuji.sadk.algorithm.sm2;

import com.myzuji.sadk.algorithm.common.GMObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm.SM2Params;
import com.myzuji.sadk.org.bouncycastle.jce.interfaces.ECPublicKey;
import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public final class SM2PublicKey implements ECPublicKey {
    private final ECParameterSpec sm2ParameterSpec;
    private static final long serialVersionUID = -4714957801525215906L;
    private byte[] bPubX;
    private byte[] bPubY;
    private BigInteger iPubX;
    private BigInteger iPubY;
    private boolean withCompression;
    private final ECPoint Q;
    private final byte[] zvalue;

    public SM2PublicKey(byte[] encoded) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.bPubX = null;
        this.bPubY = null;
        this.iPubX = null;
        this.iPubY = null;
        this.withCompression = false;
        if (encoded != null && encoded.length >= 64) {
            byte[] coding = null;
            if (encoded.length != 64 && encoded.length != 65) {
                try {
                    AlgorithmIdentifier aid = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, GMObjectIdentifiers.sm2);
                    SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(encoded);
                    if (!aid.equals(info.getAlgorithm())) {
                        throw new SecurityException("encoded not valid");
                    }

                    coding = info.getPublicKeyData().getBytes();
                    if (coding.length != 65) {
                        throw new SecurityException("encoded not valid");
                    }
                } catch (SecurityException var5) {
                    SecurityException e = var5;
                    throw e;
                } catch (Exception var6) {
                    Exception e = var6;
                    throw new RuntimeException("encoded not valid", e);
                }
            } else {
                coding = (byte[]) ((byte[]) encoded.clone());
            }

            int offset = encoded.length == 64 ? 0 : 1;
            this.bPubX = new byte[32];
            this.bPubY = new byte[32];
            System.arraycopy(coding, offset, this.bPubX, 0, this.bPubX.length);
            offset += this.bPubY.length;
            System.arraycopy(coding, offset, this.bPubY, 0, this.bPubY.length);
            this.iPubX = new BigInteger(1, this.bPubX);
            this.iPubY = new BigInteger(1, this.bPubY);
            this.Q = this.sm2ParameterSpec.getCurve().createPoint(this.iPubX, this.iPubY).normalize();
            this.zvalue = SM2Params.calcZ(this.bPubX, this.bPubY);
        } else {
            throw new IllegalArgumentException("null not allowed for encoded");
        }
    }

    public SM2PublicKey(byte[] bPubX, byte[] bPubY) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.bPubX = null;
        this.bPubY = null;
        this.iPubX = null;
        this.iPubY = null;
        this.withCompression = false;
        if (bPubX == null) {
            throw new IllegalArgumentException("null not allowed for bPubX");
        } else if (bPubY == null) {
            throw new IllegalArgumentException("null not allowed for bPubY");
        } else {
            this.Q = this.createPoint((ECPoint) null, new BigInteger(1, bPubX), new BigInteger(1, bPubY));
            this.zvalue = SM2Params.calcZ(bPubX, bPubY);
        }
    }

    public SM2PublicKey(BigInteger iPubX, BigInteger iPubY) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.bPubX = null;
        this.bPubY = null;
        this.iPubX = null;
        this.iPubY = null;
        this.withCompression = false;
        this.Q = this.createPoint((ECPoint) null, iPubX, iPubY);
        this.zvalue = SM2Params.calcZ(this.bPubX, this.bPubY);
    }

    public SM2PublicKey(ECPublicKeyParameters ecParams) {
        this.sm2ParameterSpec = SM2Params.sm2ParameterSpec;
        this.bPubX = null;
        this.bPubY = null;
        this.iPubX = null;
        this.iPubY = null;
        this.withCompression = false;
        if (ecParams == null) {
            throw new IllegalArgumentException("null not allowed for ecParams");
        } else if (!SM2Params.sm2DomainParameters.equals(ecParams.getParameters())) {
            throw new IllegalArgumentException("domainParameters not allowed for ecParams");
        } else {
            this.Q = this.createPoint(ecParams.getQ(), (BigInteger) null, (BigInteger) null);
            this.zvalue = SM2Params.calcZ(this.bPubX, this.bPubY);
        }
    }

    private final ECPoint createPoint(ECPoint point, BigInteger iPubX, BigInteger iPubY) {
        ECPoint Q = null;
        if (point != null) {
            Q = point.normalize();
            this.iPubX = Q.getXCoord().toBigInteger();
            this.iPubY = Q.getYCoord().toBigInteger();
        } else {
            if (iPubX == null) {
                throw new IllegalArgumentException("null not allowed for iPubX");
            }

            if (iPubY == null) {
                throw new IllegalArgumentException("null not allowed for iPubY");
            }

            this.iPubX = iPubX;
            this.iPubY = iPubY;
            Q = this.sm2ParameterSpec.getCurve().createPoint(iPubX, iPubY);
        }

        this.bPubX = BigIntegerUtil.asUnsigned32ByteArray(this.iPubX);
        this.bPubY = BigIntegerUtil.asUnsigned32ByteArray(this.iPubY);
        return Q;
    }

    public byte[] getEncoded() {
        try {
            AlgorithmIdentifier algorithm = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, GMObjectIdentifiers.sm2);
            byte[] encoded = new byte[65];
            encoded[0] = 4;
            System.arraycopy(this.bPubX, 0, encoded, 1, 32);
            System.arraycopy(this.bPubY, 0, encoded, 33, 32);
            SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(algorithm, encoded);
            return info.getEncoded("DER");
        } catch (Exception var4) {
            Exception e = var4;
            throw new RuntimeException("Encoded Failure for SM2PublicKey", e);
        }
    }

    public ECPoint getQ() {
        return this.Q;
    }

    public void setPointFormat(String style) {
        this.withCompression = !"UNCOMPRESSED".equalsIgnoreCase(style);
    }

    public byte[] getPubX() {
        return (byte[]) ((byte[]) this.bPubX.clone());
    }

    public BigInteger getPubX_Int() {
        return this.iPubX;
    }

    public byte[] getPubXByBytes() {
        return (byte[]) ((byte[]) this.bPubX.clone());
    }

    public BigInteger getPubXByInt() {
        return this.iPubX;
    }

    public byte[] getPubY() {
        return (byte[]) ((byte[]) this.bPubY.clone());
    }

    public BigInteger getPubY_Int() {
        return this.iPubY;
    }

    public byte[] getPubYByBytes() {
        return (byte[]) ((byte[]) this.bPubY.clone());
    }

    public BigInteger getPubYByInt() {
        return this.iPubY;
    }

    public byte[] getZvalue() {
        return (byte[]) ((byte[]) this.zvalue.clone());
    }

    public boolean isWithCompression() {
        return this.withCompression;
    }

    public String getAlgorithm() {
        return "SM2";
    }

    public String getFormat() {
        return "X.509";
    }

    public ECParameterSpec getParams() {
        return this.sm2ParameterSpec;
    }

    public ECParameterSpec getParameters() {
        return this.sm2ParameterSpec;
    }

    public int hashCode() {
        int prime = 0;
        int result = 1;
        result = 31 * result + (this.iPubX == null ? 0 : this.iPubX.hashCode());
        result = 31 * result + (this.iPubY == null ? 0 : this.iPubY.hashCode());
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
            SM2PublicKey other = (SM2PublicKey) obj;
            if (this.iPubX == null) {
                if (other.iPubX != null) {
                    return false;
                }
            } else if (!this.iPubX.equals(other.iPubX)) {
                return false;
            }

            if (this.iPubY == null) {
                if (other.iPubY != null) {
                    return false;
                }
            } else if (!this.iPubY.equals(other.iPubY)) {
                return false;
            }

            return true;
        }
    }

    public String toString() {
        String separator = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        buffer.append("SM2 Public Key: ");
        buffer.append(separator);
        buffer.append("X:");
        if (this.iPubX != null) {
            buffer.append(this.iPubX.toString(16));
        }

        buffer.append(separator);
        buffer.append("Y:");
        if (this.iPubY != null) {
            buffer.append(this.iPubY.toString(16));
        }

        return buffer.toString();
    }

    public final byte[] getDefaultZ() {
        return (byte[]) ((byte[]) this.zvalue.clone());
    }

    public final byte[] calcZ(byte[] userId) {
        return SM2Params.calcZ(this.bPubX, this.bPubY, userId);
    }

    public byte[] xBytes() {
        return (byte[]) ((byte[]) this.bPubX.clone());
    }

    public byte[] yBytes() {
        return (byte[]) ((byte[]) this.bPubY.clone());
    }
}
