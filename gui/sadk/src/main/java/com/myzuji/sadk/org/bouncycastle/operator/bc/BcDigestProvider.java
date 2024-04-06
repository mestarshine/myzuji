package com.myzuji.sadk.org.bouncycastle.operator.bc;

import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.ExtendedDigest;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;

public interface BcDigestProvider {
    ExtendedDigest get(AlgorithmIdentifier var1) throws OperatorCreationException;
}
