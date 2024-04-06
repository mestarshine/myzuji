package com.myzuji.sadk.org.bouncycastle.crypto.util;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.algorithm.common.X9ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.ElGamalParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.DHParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.pkcs.RSAPublicKey;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DSAParameter;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x9.*;
import com.myzuji.sadk.org.bouncycastle.crypto.ec.CustomNamedCurves;
import com.myzuji.sadk.org.bouncycastle.crypto.params.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class PublicKeyFactory {
    public PublicKeyFactory() {
    }

    public static AsymmetricKeyParameter createKey(byte[] keyInfoData) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(keyInfoData)));
    }

    public static AsymmetricKeyParameter createKey(InputStream inStr) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance((new ASN1InputStream(inStr)).readObject()));
    }

    public static AsymmetricKeyParameter createKey(SubjectPublicKeyInfo keyInfo) throws IOException {
        AlgorithmIdentifier algId = keyInfo.getAlgorithm();
        if (!algId.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption) && !algId.getAlgorithm().equals(X509ObjectIdentifiers.id_ea_rsa)) {
            if (algId.getAlgorithm().equals(X9ObjectIdentifiers.dhpublicnumber)) {
                DHPublicKey dhPublicKey = DHPublicKey.getInstance(keyInfo.parsePublicKey());
                BigInteger y = dhPublicKey.getY().getValue();
                DHDomainParameters dhParams = DHDomainParameters.getInstance(algId.getParameters());
                BigInteger p = dhParams.getP().getValue();
                BigInteger g = dhParams.getG().getValue();
                BigInteger q = dhParams.getQ().getValue();
                BigInteger j = null;
                if (dhParams.getJ() != null) {
                    j = dhParams.getJ().getValue();
                }

                DHValidationParameters validation = null;
                DHValidationParms dhValidationParms = dhParams.getValidationParms();
                if (dhValidationParms != null) {
                    byte[] seed = dhValidationParms.getSeed().getBytes();
                    BigInteger pgenCounter = dhValidationParms.getPgenCounter().getValue();
                    validation = new DHValidationParameters(seed, pgenCounter.intValue());
                }

                return new DHPublicKeyParameters(y, new DHParameters(p, g, q, j, validation));
            } else {
                ASN1Integer derY;
                if (algId.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
                    DHParameter params = DHParameter.getInstance(algId.getParameters());
                    derY = (ASN1Integer) keyInfo.parsePublicKey();
                    BigInteger lVal = params.getL();
                    int l = lVal == null ? 0 : lVal.intValue();
                    DHParameters dhParams = new DHParameters(params.getP(), params.getG(), (BigInteger) null, l);
                    return new DHPublicKeyParameters(derY.getValue(), dhParams);
                } else if (algId.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
                    ElGamalParameter params = ElGamalParameter.getInstance(algId.getParameters());
                    derY = (ASN1Integer) keyInfo.parsePublicKey();
                    return new ElGamalPublicKeyParameters(derY.getValue(), new ElGamalParameters(params.getP(), params.getG()));
                } else if (!algId.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa) && !algId.getAlgorithm().equals(OIWObjectIdentifiers.dsaWithSHA1)) {
                    if (algId.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
                        X962Parameters params = X962Parameters.getInstance(algId.getParameters());
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

                        ASN1OctetString key = new DEROctetString(keyInfo.getPublicKeyData().getBytes());
                        X9ECPoint derQ = new X9ECPoint(x9.getCurve(), key);
                        return new ECPublicKeyParameters(derQ.getPoint(), (ECDomainParameters) dParams);
                    } else {
                        throw new RuntimeException("algorithm identifier in key not recognised");
                    }
                } else {
                    derY = (ASN1Integer) keyInfo.parsePublicKey();
                    ASN1Encodable de = algId.getParameters();
                    DSAParameters parameters = null;
                    if (de != null) {
                        DSAParameter params = DSAParameter.getInstance(de.toASN1Primitive());
                        parameters = new DSAParameters(params.getP(), params.getQ(), params.getG());
                    }

                    return new DSAPublicKeyParameters(derY.getValue(), parameters);
                }
            }
        } else {
            RSAPublicKey pubKey = RSAPublicKey.getInstance(keyInfo.parsePublicKey());
            return new RSAKeyParameters(false, pubKey.getModulus(), pubKey.getPublicExponent());
        }
    }
}
