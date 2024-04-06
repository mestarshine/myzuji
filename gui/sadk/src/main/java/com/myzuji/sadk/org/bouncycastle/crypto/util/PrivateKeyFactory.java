package com.myzuji.sadk.org.bouncycastle.crypto.util;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.ElGamalParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.DHParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import com.myzuji.sadk.org.bouncycastle.asn1.sec.ECPrivateKey;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DSAParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.ECNamedCurveTable;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X962Parameters;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.X9ECParameters;
import com.myzuji.sadk.org.bouncycastle.crypto.ec.CustomNamedCurves;
import com.myzuji.sadk.org.bouncycastle.crypto.params.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class PrivateKeyFactory {
    public PrivateKeyFactory() {
    }

    public static AsymmetricKeyParameter createKey(byte[] privateKeyInfoData) throws IOException {
        return createKey(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(privateKeyInfoData)));
    }

    public static AsymmetricKeyParameter createKey(InputStream inStr) throws IOException {
        return createKey(PrivateKeyInfo.getInstance((new ASN1InputStream(inStr)).readObject()));
    }

    public static AsymmetricKeyParameter createKey(PrivateKeyInfo keyInfo) throws IOException {
        AlgorithmIdentifier algId = keyInfo.getPrivateKeyAlgorithm();
        if (algId.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            RSAPrivateKey keyStructure = RSAPrivateKey.getInstance(keyInfo.parsePrivateKey());
            return new RSAPrivateCrtKeyParameters(keyStructure.getModulus(), keyStructure.getPublicExponent(), keyStructure.getPrivateExponent(), keyStructure.getPrime1(), keyStructure.getPrime2(), keyStructure.getExponent1(), keyStructure.getExponent2(), keyStructure.getCoefficient());
        } else {
            ASN1Integer derX;
            if (algId.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
                DHParameter params = DHParameter.getInstance(algId.getParameters());
                derX = (ASN1Integer) keyInfo.parsePrivateKey();
                BigInteger lVal = params.getL();
                int l = lVal == null ? 0 : lVal.intValue();
                DHParameters dhParams = new DHParameters(params.getP(), params.getG(), (BigInteger) null, l);
                return new DHPrivateKeyParameters(derX.getValue(), dhParams);
            } else if (algId.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
                ElGamalParameter params = ElGamalParameter.getInstance(algId.getParameters());
                derX = (ASN1Integer) keyInfo.parsePrivateKey();
                return new ElGamalPrivateKeyParameters(derX.getValue(), new ElGamalParameters(params.getP(), params.getG()));
            } else if (algId.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa)) {
                derX = (ASN1Integer) keyInfo.parsePrivateKey();
                ASN1Encodable de = algId.getParameters();
                DSAParameters parameters = null;
                if (de != null) {
                    DSAParameter params = DSAParameter.getInstance(de.toASN1Primitive());
                    parameters = new DSAParameters(params.getP(), params.getQ(), params.getG());
                }

                return new DSAPrivateKeyParameters(derX.getValue(), parameters);
            } else if (algId.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
                X962Parameters params = new X962Parameters((ASN1Primitive) algId.getParameters());
                X9ECParameters x9;
                Object dParams;
                if (params.isNamedCurve()) {
                    ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) params.getParameters();
                    x9 = CustomNamedCurves.getByOID(oid);
                    if (x9 == null) {
                        x9 = ECNamedCurveTable.getByOID(oid);
                    }

                    dParams = new ECNamedDomainParameters(oid, x9.getCurve(), x9.getG(), x9.getN(), x9.getH(), x9.getSeed());
                } else {
                    x9 = X9ECParameters.getInstance(params.getParameters());
                    dParams = new ECDomainParameters(x9.getCurve(), x9.getG(), x9.getN(), x9.getH(), x9.getSeed());
                }

                ECPrivateKey ec = ECPrivateKey.getInstance(keyInfo.parsePrivateKey());
                BigInteger d = ec.getKey();
                return new ECPrivateKeyParameters(d, (ECDomainParameters) dParams);
            } else {
                throw new RuntimeException("algorithm identifier in key not recognised");
            }
        }
    }
}
