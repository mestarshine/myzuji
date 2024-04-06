package com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.sm;

import com.myzuji.sadk.asn1.DERHeader;
import com.myzuji.sadk.org.bouncycastle.asn1.sm2.ASN1SM2Cipher;
import com.myzuji.sadk.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.myzuji.sadk.org.bouncycastle.math.ec.ECPoint;
import com.myzuji.sadk.org.bouncycastle.util.BigIntegers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class Crypto {
    private SM3Digest keySM3Engine;
    private SM3Digest datSM3Engine;
    private BigInteger userD;
    private BigInteger C1XCoord = null;
    private BigInteger C1YCoord = null;
    private byte[] P2XBytes = null;
    private byte[] P2YBytes = null;
    private byte[] key = new byte[32];
    private byte keyOff = 0;
    private int ct = 1;

    public Crypto() {
    }

    private void reset() {
        this.keySM3Engine = new SM3Digest();
        this.datSM3Engine = new SM3Digest();
        this.keySM3Engine.update(this.P2XBytes, 0, this.P2XBytes.length);
        this.keySM3Engine.update(this.P2YBytes, 0, this.P2YBytes.length);
        this.datSM3Engine.update(this.P2XBytes, 0, this.P2XBytes.length);
        this.ct = 1;
        this.nextKey();
    }

    private void nextKey() {
        SM3Digest sm3keycur = new SM3Digest(this.keySM3Engine);
        sm3keycur.update((byte) (this.ct >> 24 & 255));
        sm3keycur.update((byte) (this.ct >> 16 & 255));
        sm3keycur.update((byte) (this.ct >> 8 & 255));
        sm3keycur.update((byte) (this.ct & 255));
        sm3keycur.doFinal(this.key, 0);
        this.keyOff = 0;
        ++this.ct;
    }

    public void initEncrypt(ECPoint userKey) {
        if (userKey == null) {
            throw new SecurityException("null/length not allowed for userKey");
        } else {
            AsymmetricCipherKeyPair key = SM2Params.generators.generateKeyPair();
            ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
            ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
            BigInteger k = ecpriv.getD();
            ECPoint C1 = ecpub.getQ().normalize();
            this.C1XCoord = C1.getXCoord().toBigInteger();
            this.C1YCoord = C1.getYCoord().toBigInteger();
            ECPoint P2 = userKey.multiply(k).normalize();
            this.P2XBytes = BigIntegers.asUnsignedByteArray(32, P2.getXCoord().toBigInteger());
            this.P2YBytes = BigIntegers.asUnsignedByteArray(32, P2.getYCoord().toBigInteger());
            this.reset();
        }
    }

    public void initDecrypt(BigInteger userD) {
        if (userD == null) {
            throw new SecurityException("null/length not allowed for userD");
        } else {
            this.userD = userD;
        }
    }

    public byte[] encrypt(byte[] sourceData) {
        if (sourceData == null) {
            throw new SecurityException("null/length not allowed for sourceData");
        } else {
            try {
                byte[] data = (byte[]) ((byte[]) sourceData.clone());
                this.datSM3Engine.update(data, 0, data.length);

                for (int i = 0; i < data.length; ++i) {
                    if (this.keyOff == this.key.length) {
                        this.nextKey();
                    }

                    byte var10002 = data[i];
                    byte[] var10003 = this.key;
                    byte var10006 = this.keyOff;
                    this.keyOff = (byte) (var10006 + 1);
                    data[i] = (byte) (var10002 ^ var10003[var10006]);
                }

                byte[] hashValue = new byte[32];
                this.dofinal(hashValue);
                ASN1SM2Cipher asn1 = new ASN1SM2Cipher(this.C1XCoord, this.C1YCoord, hashValue, data);
                return asn1.getEncoded("DER");
            } catch (IOException var5) {
                IOException e = var5;
                throw new RuntimeException("encrypt failure", e);
            }
        }
    }

    public byte[] decrypt(byte[] encryptData) throws Exception {
        if (encryptData != null && encryptData.length >= 96) {
            DERHeader der = null;

            try {
                der = new DERHeader(encryptData, 0);
            } catch (Exception var10) {
                der = null;
            }

            BigInteger x = null;
            BigInteger y = null;
            byte[] encryptedBytes = null;
            byte[] hash = null;
            byte[] c2DecryptedBytes;
            if (der != null && der.getTag() == 48 && der.getDerLength() == encryptData.length) {
                ASN1SM2Cipher encoding = ASN1SM2Cipher.getInstance(encryptData);
                x = encoding.getXCoordinate().getValue();
                y = encoding.getYCoordinate().getValue();
                encryptedBytes = encoding.getCipherText().getOctets();
                hash = encoding.getHashValue().getOctets();
                c2DecryptedBytes = this.decrypt(x, y, encryptedBytes, hash);
                if (c2DecryptedBytes == null) {
                    throw new IOException("can not decrypt, Check if the encrypt block is right");
                } else {
                    return c2DecryptedBytes;
                }
            } else {
                byte[] c1Xbyte = new byte[32];
                c2DecryptedBytes = new byte[32];
                System.arraycopy(encryptData, 0, c1Xbyte, 0, 32);
                System.arraycopy(encryptData, 32, c2DecryptedBytes, 0, 32);
                x = new BigInteger(1, c1Xbyte);
                y = new BigInteger(1, c2DecryptedBytes);
                encryptedBytes = new byte[encryptData.length - 96];
                hash = new byte[32];
                System.arraycopy(encryptData, 64, hash, 0, 32);
                System.arraycopy(encryptData, 96, encryptedBytes, 0, encryptedBytes.length);
                c2DecryptedBytes = this.decrypt(x, y, encryptedBytes, hash);
                if (c2DecryptedBytes == null) {
                    System.arraycopy(encryptData, 64, encryptedBytes, 0, encryptedBytes.length);
                    System.arraycopy(encryptData, 64 + encryptedBytes.length, hash, 0, 32);
                    c2DecryptedBytes = this.decrypt(x, y, encryptedBytes, hash);
                }

                if (c2DecryptedBytes == null) {
                    throw new IOException("can not decrypt, Check if the encrypt block is right");
                } else {
                    return c2DecryptedBytes;
                }
            }
        } else {
            throw new SecurityException("null/length not allowed for SM2EncryptData");
        }
    }

    private final byte[] decrypt(BigInteger x, BigInteger y, byte[] encryptedBytes, byte[] hash) throws IOException {
        ECPoint c1 = SM2Params.sm2ParameterSpec.getCurve().createPoint(x, y);
        ECPoint P2 = c1.multiply(this.userD).normalize();
        this.P2XBytes = BigIntegers.asUnsignedByteArray(32, P2.getXCoord().toBigInteger());
        this.P2YBytes = BigIntegers.asUnsignedByteArray(32, P2.getYCoord().toBigInteger());
        this.reset();
        byte[] decrypted = (byte[]) ((byte[]) encryptedBytes.clone());

        for (int i = 0; i < decrypted.length; ++i) {
            if (this.keyOff == this.key.length) {
                this.nextKey();
            }

            byte var10002 = decrypted[i];
            byte[] var10003 = this.key;
            byte var10006 = this.keyOff;
            this.keyOff = (byte) (var10006 + 1);
            decrypted[i] = (byte) (var10002 ^ var10003[var10006]);
        }

        this.datSM3Engine.update(decrypted, 0, decrypted.length);
        byte[] C3Hash = new byte[32];
        this.dofinal(C3Hash);
        if (Arrays.equals(C3Hash, hash)) {
            return decrypted;
        } else {
            return null;
        }
    }

    private void dofinal(byte[] C2Hash) {
        this.datSM3Engine.update(this.P2YBytes, 0, this.P2YBytes.length);
        this.datSM3Engine.doFinal(C2Hash, 0);
        this.reset();
    }
}
