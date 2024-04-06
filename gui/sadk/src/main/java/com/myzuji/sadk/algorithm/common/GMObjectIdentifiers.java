package com.myzuji.sadk.algorithm.common;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface GMObjectIdentifiers {
    ASN1ObjectIdentifier sm2 = new ASN1ObjectIdentifier("1.2.156.10197.1.301");
    ASN1ObjectIdentifier sm2Sign = sm2.branch("1");
    ASN1ObjectIdentifier sm2KeyExchange = sm2.branch("2");
    ASN1ObjectIdentifier sm2PublicKeyEncrypt = sm2.branch("3");
    ASN1ObjectIdentifier sm2Base = new ASN1ObjectIdentifier("1.2.156.10197.6.1.4.2");
    ASN1ObjectIdentifier sm2Data = sm2Base.branch("1");
    ASN1ObjectIdentifier sm2SignedData = sm2Base.branch("2");
    ASN1ObjectIdentifier sm2EnvelopedData = sm2Base.branch("3");
    ASN1ObjectIdentifier sm2SignedAndEnvelopedData = sm2Base.branch("4");
    ASN1ObjectIdentifier sm2EncryptedData = sm2Base.branch("5");
    ASN1ObjectIdentifier sm2KeyAgreementInfo = sm2Base.branch("6");
    ASN1ObjectIdentifier sm3WithSM2Encryption = new ASN1ObjectIdentifier("1.2.156.10197.1.501");
    ASN1ObjectIdentifier SM3WithSM2Encryption = sm3WithSM2Encryption;
    ASN1ObjectIdentifier sm3WithSM2Encryption_old = new ASN1ObjectIdentifier("1.2.156.197.1.501");
    ASN1ObjectIdentifier sm2_old = new ASN1ObjectIdentifier("1.2.156.197.1.301");
    ASN1ObjectIdentifier sm2PubKey = new ASN1ObjectIdentifier("1.2.156.10197.1.301");
    ASN1ObjectIdentifier ecPubKey = X9ObjectIdentifiers.id_ecPublicKey;
    ASN1ObjectIdentifier sm3 = new ASN1ObjectIdentifier("1.2.156.10197.1.401");
    ASN1ObjectIdentifier id_sm4_CBC = new ASN1ObjectIdentifier("1.2.156.10197.1.104");
    ASN1ObjectIdentifier id_sm4_ECB = new ASN1ObjectIdentifier("1.2.156.10197.1.104.1");
}
