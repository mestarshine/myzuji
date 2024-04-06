package com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.rsa;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1Encodable;
import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.crypto.params.RSAKeyParameters;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import com.myzuji.sadk.org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import com.myzuji.sadk.org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Enumeration;

public class BCRSAPrivateKey implements RSAPrivateKey, PKCS12BagAttributeCarrier {
    static final long serialVersionUID = 5110188922551353628L;
    private static BigInteger ZERO = BigInteger.valueOf(0L);
    protected BigInteger modulus;
    protected BigInteger privateExponent;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();

    protected BCRSAPrivateKey() {
    }

    BCRSAPrivateKey(RSAKeyParameters key) {
        this.modulus = key.getModulus();
        this.privateExponent = key.getExponent();
    }

    BCRSAPrivateKey(RSAPrivateKeySpec spec) {
        this.modulus = spec.getModulus();
        this.privateExponent = spec.getPrivateExponent();
    }

    BCRSAPrivateKey(RSAPrivateKey key) {
        this.modulus = key.getModulus();
        this.privateExponent = key.getPrivateExponent();
    }

    BCRSAPrivateKey(com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPrivateKey key) {
        this.modulus = key.getModulus();
        this.privateExponent = key.getPrivateExponent();
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }

    public String getAlgorithm() {
        return "RSA";
    }

    public String getFormat() {
        return "PKCS#8";
    }

    public byte[] getEncoded() {
        return KeyUtil.getEncodedPrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPrivateKey(this.getModulus(), ZERO, this.getPrivateExponent(), ZERO, ZERO, ZERO, ZERO, ZERO));
    }

    public boolean equals(Object o) {
        if (!(o instanceof RSAPrivateKey)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            RSAPrivateKey key = (RSAPrivateKey) o;
            return this.getModulus().equals(key.getModulus()) && this.getPrivateExponent().equals(key.getPrivateExponent());
        }
    }

    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPrivateExponent().hashCode();
    }

    public void setBagAttribute(ASN1ObjectIdentifier oid, ASN1Encodable attribute) {
        this.attrCarrier.setBagAttribute(oid, attribute);
    }

    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier oid) {
        return this.attrCarrier.getBagAttribute(oid);
    }

    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
