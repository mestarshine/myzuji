package com.myzuji.sadk.signature.sm2;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.algorithm.sm2.SM2PrivateKey;
import com.myzuji.sadk.algorithm.sm2.SM2PublicKey;
import com.myzuji.sadk.algorithm.sm2.SM2Result;
import com.myzuji.sadk.algorithm.util.BigIntegerUtil;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftSM2;
import com.myzuji.sadk.lib.crypto.jni.JNIDigest;
import com.myzuji.sadk.lib.crypto.jni.JNISM2;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Integer;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Sequence;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.SM3Digest;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SM2Signature {
    private SM3Digest digest = new SM3Digest();
    private BCSoftSM2 sm2;
    private SM2Result sm2Ret = new SM2Result();
    private JNIDigest sm3_jni;
    private SM2PrivateKey priKey;
    private SM2PublicKey pubKey;
    private byte[] da = new byte[32];
    private byte[] pubx = new byte[32];
    private byte[] puby = new byte[32];
    private boolean useJNI = false;

    public SM2Signature() throws PKIException {
        if (this.useJNI) {
            try {
                this.sm3_jni = new JNIDigest();
                this.sm3_jni.init(922);
            } catch (Exception var2) {
                Exception e = var2;
                throw new PKIException("JNIDigest Init Failure: ", e);
            }
        } else {
            this.sm2 = new BCSoftSM2();
        }

    }

    public void initSign(PrivateKey arg0) throws PKIException {
        if (arg0 instanceof SM2PrivateKey) {
            this.priKey = (SM2PrivateKey) arg0;
            if (this.useJNI) {
                byte[] temp = BigIntegerUtil.asUnsigned32ByteArray(this.priKey.getDByInt());
                System.arraycopy(temp, 0, this.da, 0, 32);
            }

        } else {
            throw new PKIException("The private key type is not sm2 type!");
        }
    }

    public void initVerify(PublicKey publicKey) throws PKIException {
        try {
            if (!(publicKey instanceof SM2PublicKey)) {
                throw new PKIException("Can't recognise key type in SM2 based signer");
            }

            this.pubKey = (SM2PublicKey) publicKey;
        } catch (Exception var4) {
            throw new PKIException("Can't recognise key type in SM2 based signer");
        }

        if (this.useJNI) {
            byte[] tempX = this.pubKey.getPubXByBytes();
            byte[] tempY = this.pubKey.getPubYByBytes();
            System.arraycopy(tempX, 0, this.pubx, 0, 32);
            System.arraycopy(tempY, 0, this.puby, 0, 32);
        }

    }

    public byte[] sign(byte[] sourceData) throws PKIException {
        byte[] out = new byte[32];
        byte[] sign = new byte[64];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        if (this.useJNI) {
            try {
                this.sm3_jni.update(sourceData);
                this.sm3_jni.doFinal(out);
                JNISM2.sign(out, this.da, r, s);
            } catch (PKIException var7) {
                PKIException e = var7;
                throw e;
            } catch (Exception var8) {
                Exception e = var8;
                throw new PKIException("Signed failure", e);
            }

            System.arraycopy(r, 0, sign, 0, 32);
            System.arraycopy(s, 0, sign, 32, 32);
        } else {
            this.digest.update(sourceData, 0, sourceData.length);
            this.digest.doFinal(out, 0);
            this.sm2.sign(out, this.priKey.getDByInt(), this.sm2Ret);
            System.arraycopy(BigIntegerUtil.asUnsigned32ByteArray(this.sm2Ret.r), 0, sign, 0, 32);
            System.arraycopy(BigIntegerUtil.asUnsigned32ByteArray(this.sm2Ret.s), 0, sign, 32, 32);
        }

        return sign;
    }

    public void update(byte arg0) throws PKIException {
        if (this.useJNI) {
            byte[] temp = new byte[]{arg0};

            try {
                this.sm3_jni.update(temp);
            } catch (PKIException var4) {
                PKIException e = var4;
                throw e;
            } catch (Exception var5) {
                Exception e = var5;
                throw new PKIException("SM3 Digest failure", e);
            }
        } else {
            this.digest.update(arg0);
        }

    }

    public void update(byte[] arg0, int arg1, int arg2) throws PKIException {
        if (this.useJNI) {
            int len = arg0.length;
            byte[] temp;
            if (arg1 + arg2 > len) {
                temp = new byte[len];
                System.arraycopy(arg0, 0, temp, 0, len);
            } else {
                temp = new byte[arg2];
                System.arraycopy(arg0, arg1, temp, 0, arg2);
            }

            try {
                this.sm3_jni.update(temp);
            } catch (PKIException var7) {
                PKIException e = var7;
                throw e;
            } catch (Exception var8) {
                Exception e = var8;
                throw new PKIException("SM3 Digest failure", e);
            }
        } else {
            this.digest.update(arg0, arg1, arg2);
        }

    }

    public boolean verify(byte[] signature, byte[] sourceData) throws PKIException {
        byte[] out = new byte[32];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        if (signature.length == 64) {
            System.arraycopy(signature, 0, r, 0, 32);
            System.arraycopy(signature, 32, s, 0, 32);
        } else {
            if (signature.length <= 64) {
                return false;
            }

            ASN1Sequence sequence = ASN1Sequence.getInstance(signature);
            ASN1Integer R = (ASN1Integer) sequence.getObjectAt(0);
            ASN1Integer S = (ASN1Integer) sequence.getObjectAt(1);
            r = BigIntegerUtil.asUnsigned32ByteArray(R.getPositiveValue());
            s = BigIntegerUtil.asUnsigned32ByteArray(S.getPositiveValue());
        }

        if (this.useJNI) {
            try {
                this.sm3_jni.update(sourceData);
                this.sm3_jni.doFinal(out);
                return JNISM2.verify(r, s, this.pubx, this.puby, out);
            } catch (Exception var9) {
                return false;
            }
        } else {
            this.digest.update(sourceData, 0, sourceData.length);
            this.digest.doFinal(out, 0);
            this.sm2Ret.r = new BigInteger(1, r);
            this.sm2Ret.s = new BigInteger(1, s);
            return this.sm2.verify(out, this.pubKey.getQ(), this.sm2Ret);
        }
    }
}
