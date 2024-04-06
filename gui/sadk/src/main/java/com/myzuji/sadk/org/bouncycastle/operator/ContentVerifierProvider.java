package com.myzuji.sadk.org.bouncycastle.operator;

import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.cert.X509CertificateHolder;

public interface ContentVerifierProvider {
    boolean hasAssociatedCertificate();

    X509CertificateHolder getAssociatedCertificate();

    ContentVerifier get(AlgorithmIdentifier var1) throws OperatorCreationException;
}
