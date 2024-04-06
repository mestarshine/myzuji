package com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.rsa;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;

import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

public class BCRSAPublicKey implements RSAPublicKey {
    private static final AlgorithmIdentifier DEFAULT_ALGORITHM_IDENTIFIER;
    static final long serialVersionUID = 2675817738516720772L;
    private BigInteger modulus;
    private BigInteger publicExponent;
    private transient AlgorithmIdentifier algorithmIdentifier;

    public BCRSAPublicKey(RSAKeyParameters key) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = key.getModulus();
        this.publicExponent = key.getExponent();
    }

    BCRSAPublicKey(RSAPublicKeySpec spec) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = spec.getModulus();
        this.publicExponent = spec.getPublicExponent();
    }

    BCRSAPublicKey(RSAPublicKey key) {
        this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = key.getModulus();
        this.publicExponent = key.getPublicExponent();
    }

    BCRSAPublicKey(SubjectPublicKeyInfo info) {
        this.populateFromPublicKeyInfo(info);
    }

    private void populateFromPublicKeyInfo(SubjectPublicKeyInfo info) {
        try {
            com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPublicKey pubKey = com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(info.parsePublicKey());
            this.algorithmIdentifier = info.getAlgorithm();
            this.modulus = pubKey.getModulus();
            this.publicExponent = pubKey.getPublicExponent();
        } catch (IOException var3) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public String getFormat() {
        return "X.509";
    }

    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(this.algorithmIdentifier, new com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPublicKey(this.getModulus(), this.getPublicExponent()));
    }

    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RSAPublicKey)) {
            return false;
        } else {
            RSAPublicKey key = (RSAPublicKey) o;
            return this.getModulus().equals(key.getModulus()) && this.getPublicExponent().equals(key.getPublicExponent());
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String nl = System.getProperty("line.separator");
        buf.append("RSA Public Key").append(nl);
        buf.append("            modulus: ").append(this.getModulus().toString(16)).append(nl);
        buf.append("    public exponent: ").append(this.getPublicExponent().toString(16)).append(nl);
        return buf.toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        try {
            this.algorithmIdentifier = AlgorithmIdentifier.getInstance(in.readObject());
        } catch (OptionalDataException var3) {
            this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        } catch (EOFException var4) {
            this.algorithmIdentifier = DEFAULT_ALGORITHM_IDENTIFIER;
        }

    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (!this.algorithmIdentifier.equals(DEFAULT_ALGORITHM_IDENTIFIER)) {
            out.writeObject(this.algorithmIdentifier.getEncoded());
        }

    }

    static {
        DEFAULT_ALGORITHM_IDENTIFIER = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
    }
}
