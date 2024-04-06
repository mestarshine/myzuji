package com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm;

import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.generators.SM2KeyPairGenerator;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECDomainParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import com.myzuji.sadk.org.bouncycastle.jce.spec.ECParameterSpec;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECCurve;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public final class SM2Params {
    public static final BigInteger ONE = BigInteger.valueOf(1L);
    public static final BigInteger TWO = BigInteger.valueOf(2L);
    public static final BigInteger p = fromHex("FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF 00000000 FFFFFFFF FFFFFFFF");
    public static final BigInteger a = fromHex("FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF 00000000 FFFFFFFF FFFFFFFC");
    public static final BigInteger b = fromHex("28E9FA9E 9D9F5E34 4D5A9E4B CF6509A7 F39789F5 15AB8F92 DDBCBD41 4D940E93");
    public static final BigInteger n = fromHex("FFFFFFFE FFFFFFFF FFFFFFFF FFFFFFFF 7203DF6B 21C6052B 53BBF409 39D54123");
    public static final BigInteger gx = fromHex("32C4AE2C 1F198119 5F990446 6A39C994 8FE30BBF F2660BE1 715A4589 334C74C7");
    public static final BigInteger gy = fromHex("BC3736A2 F4F6779C 59BDCEE3 6B692153 D0A9877C C62A4740 02DF32E5 2139F0A0");
    public static final BigInteger h = BigInteger.valueOf(1L);
    static final byte[] pBytes;
    static final byte[] aBytes;
    static final byte[] bBytes;
    static final byte[] nBytes;
    static final byte[] GxBytes;
    static final byte[] GyBytes;
    static final byte[] defaultUserId;
    public static final ECParameterSpec sm2dhtest;
    public static final ECParameterSpec sm2ParameterSpec;
    public static final ECDomainParameters sm2DomainParameters;
    public static final SM2KeyPairGenerator generators;

    private SM2Params() {
    }

    static final ECParameterSpec createSM2Parameters() {
        ECCurve curve = new ECCurve.Fp(p, a, b, n, h);
        ECPoint G = curve.createPoint(gx, gy);
        return new ECParameterSpec(curve, G, n);
    }

    static final ECDomainParameters createSM2ECDomainParameters() {
        ECCurve curve = new ECCurve.Fp(p, a, b, n, h);
        ECPoint G = curve.createPoint(gx, gy);
        return new ECDomainParameters(curve, G, n);
    }

    static final SM2KeyPairGenerator createSM2Generator() {
        ECDomainParameters params = createSM2ECDomainParameters();
        ECKeyGenerationParameters gparams = new ECKeyGenerationParameters(params, new SecureRandom());
        SM2KeyPairGenerator generator = new SM2KeyPairGenerator();
        generator.init(gparams);
        return generator;
    }

    static final ECParameterSpec createSM2DHTestParameters() {
        BigInteger p = fromHex("8542D69E 4C044F18 E8B92435 BF6FF7DE 45728391 5C45517D 722EDB8B 08F1DFC3");
        BigInteger a = fromHex("787968B4 FA32C3FD 2417842E 73BBFEFF 2F3C848B 6831D7E0 EC65228B 3937E498");
        BigInteger b = fromHex("63E4C6D3 B23B0C84 9CF84241 484BFE48 F61D59A5 B16BA06E 6E12D1DA 27C5249A");
        BigInteger n = fromHex("8542D69E 4C044F18 E8B92435 BF6FF7DD 29772063 0485628D 5AE74EE7 C32E79B7");
        BigInteger gx = fromHex("421DEBD6 1B62EAB6 746434EB C3CC315E 32220B3B ADD50BDC 4C4E6C14 7FEDD43D");
        BigInteger gy = fromHex("0680512B CBB42C07 D47349D2 153B70C4 E5D7FDFC BFA36EA1 A85841B9 E46E09A2");
        BigInteger h = BigInteger.valueOf(1L);
        ECCurve curve = new ECCurve.Fp(p, a, b, n, h);
        ECPoint G = curve.createPoint(gx, gy);
        return new ECParameterSpec(curve, G, n, h);
    }

    public static final BigInteger fromHex(String hex) {
        return new BigInteger(1, Hex.decode(hex));
    }

    public static final byte[] calcZ(ECPoint Q) {
        if (Q == null) {
            throw new IllegalArgumentException("The parameter ECPoint is null");
        } else {
            ECPoint q = Q.normalize();
            return calcZ(q.getXCoord().toBigInteger(), q.getYCoord().toBigInteger(), defaultUserId);
        }
    }

    public static final byte[] calcZ(BigInteger x, BigInteger y) {
        return calcZ(x, y, defaultUserId);
    }

    public static final byte[] calcZ(ECParameterSpec spec, BigInteger x, BigInteger y) {
        return calcZ(spec, x, y, defaultUserId);
    }

    public static final byte[] calcZ(byte[] aBytes, byte[] bBytes, byte[] xGBytes, byte[] yGBytes, byte[] xBytes, byte[] yBytes) {
        return calcZ(defaultUserId, aBytes, bBytes, xGBytes, yGBytes, xBytes, yBytes);
    }

    public static final byte[] calcZ(BigInteger x, BigInteger y, byte[] idBytes) {
        if (x == null) {
            throw new IllegalArgumentException("The parameter x is null");
        } else if (y == null) {
            throw new IllegalArgumentException("The parameter y is null");
        } else {
            if (idBytes == null) {
                idBytes = (byte[]) ((byte[]) defaultUserId.clone());
            }

            byte[] xBytes = BigIntegers.asUnsignedByteArray(32, x);
            byte[] yBytes = BigIntegers.asUnsignedByteArray(32, y);
            SM3Digest digest = new SM3Digest();
            int entlen = idBytes.length * 8;
            digest.update((byte) (entlen >> 8));
            digest.update((byte) entlen);
            digest.update(idBytes, 0, idBytes.length);
            digest.update(aBytes, 0, aBytes.length);
            digest.update(bBytes, 0, bBytes.length);
            digest.update(GxBytes, 0, GxBytes.length);
            digest.update(GyBytes, 0, GyBytes.length);
            digest.update(xBytes, 0, xBytes.length);
            digest.update(yBytes, 0, yBytes.length);
            byte[] out = new byte[digest.getDigestSize()];
            digest.doFinal(out, 0);
            return out;
        }
    }

    public static final byte[] calcZ(ECParameterSpec spec, BigInteger x, BigInteger y, byte[] idBytes) {
        if (spec == null) {
            throw new IllegalArgumentException("The parameter spec is null");
        } else if (x == null) {
            throw new IllegalArgumentException("The parameter x is null");
        } else if (y == null) {
            throw new IllegalArgumentException("The parameter y is null");
        } else {
            if (idBytes == null) {
                idBytes = (byte[]) ((byte[]) defaultUserId.clone());
            }

            int entlen = idBytes.length * 8;
            int length = (spec.getCurve().getFieldSize() + 7) / 8;
            SM3Digest digest = new SM3Digest();
            digest.update((byte) (entlen >> 8));
            digest.update((byte) entlen);
            digest.update(idBytes, 0, idBytes.length);
            digest.update(BigIntegers.asUnsignedByteArray(length, spec.getCurve().getA().toBigInteger()), 0, length);
            digest.update(BigIntegers.asUnsignedByteArray(length, spec.getCurve().getB().toBigInteger()), 0, length);
            digest.update(BigIntegers.asUnsignedByteArray(length, spec.getG().getXCoord().toBigInteger()), 0, length);
            digest.update(BigIntegers.asUnsignedByteArray(length, spec.getG().getYCoord().toBigInteger()), 0, length);
            digest.update(BigIntegers.asUnsignedByteArray(length, x), 0, length);
            digest.update(BigIntegers.asUnsignedByteArray(length, y), 0, length);
            byte[] out = new byte[digest.getDigestSize()];
            digest.doFinal(out, 0);
            return out;
        }
    }

    private static final byte[] calcZ(byte[] idBytes, byte[] aBytes, byte[] bBytes, byte[] xGBytes, byte[] yGBytes, byte[] xBytes, byte[] yBytes) {
        if (idBytes == null) {
            idBytes = (byte[]) ((byte[]) defaultUserId.clone());
        }

        int entlen = idBytes.length * 8;
        SM3Digest digest = new SM3Digest();
        digest.update((byte) (entlen >> 8));
        digest.update((byte) entlen);
        digest.update(idBytes, 0, idBytes.length);
        digest.update(aBytes, 0, aBytes.length);
        digest.update(bBytes, 0, bBytes.length);
        digest.update(xGBytes, 0, xGBytes.length);
        digest.update(yGBytes, 0, yGBytes.length);
        digest.update(xBytes, 0, xBytes.length);
        digest.update(yBytes, 0, yBytes.length);
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return out;
    }

    public static byte[] getPbytes() {
        return (byte[]) ((byte[]) pBytes.clone());
    }

    public static byte[] getAbytes() {
        return (byte[]) ((byte[]) aBytes.clone());
    }

    public static byte[] getBbytes() {
        return (byte[]) ((byte[]) bBytes.clone());
    }

    public static byte[] getNbytes() {
        return (byte[]) ((byte[]) nBytes.clone());
    }

    public static byte[] getGxbytes() {
        return (byte[]) ((byte[]) GxBytes.clone());
    }

    public static byte[] getGybytes() {
        return (byte[]) ((byte[]) GyBytes.clone());
    }

    public static byte[] getDefaultuserid() {
        return (byte[]) ((byte[]) defaultUserId.clone());
    }

    public static final byte[] concat(byte[] firsts, byte[] seconds) {
        if (firsts == null) {
            firsts = new byte[0];
        }

        if (seconds == null) {
            seconds = new byte[0];
        }

        int n1 = firsts.length;
        int n2 = seconds.length;
        byte[] b = new byte[n1 + n2];
        System.arraycopy(firsts, 0, b, 0, n1);
        System.arraycopy(seconds, 0, b, n1, n2);
        return b;
    }

    public static final boolean isDefaultuserid(byte[] userId) {
        if (userId != null && userId.length == defaultUserId.length) {
            return userId == defaultUserId ? true : Arrays.equals(defaultUserId, userId);
        } else {
            return false;
        }
    }

    public static byte[] calcZ(byte[] xBytes, byte[] yBytes) {
        return calcZ(xBytes, yBytes, defaultUserId);
    }

    public static byte[] calcZ(byte[] xBytes, byte[] yBytes, byte[] userId) {
        if (xBytes != null && xBytes.length == 32) {
            if (yBytes != null && yBytes.length == 32) {
                return calcZ(userId, aBytes, bBytes, GxBytes, GyBytes, xBytes, yBytes);
            } else {
                throw new IllegalArgumentException("null/length not allowed for yBytes");
            }
        } else {
            throw new IllegalArgumentException("null/length not allowed for xBytes");
        }
    }

    static {
        pBytes = BigIntegers.asUnsignedByteArray(32, p);
        aBytes = BigIntegers.asUnsignedByteArray(32, a);
        bBytes = BigIntegers.asUnsignedByteArray(32, b);
        nBytes = BigIntegers.asUnsignedByteArray(32, n);
        GxBytes = BigIntegers.asUnsignedByteArray(32, gx);
        GyBytes = BigIntegers.asUnsignedByteArray(32, gy);
        defaultUserId = new byte[]{49, 50, 51, 52, 53, 54, 55, 56, 49, 50, 51, 52, 53, 54, 55, 56};
        sm2dhtest = createSM2DHTestParameters();
        sm2ParameterSpec = createSM2Parameters();
        sm2DomainParameters = createSM2ECDomainParameters();
        generators = createSM2Generator();
    }
}
