package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;

public interface SignerInformationVerifierProvider {
    SignerInformationVerifier get(SignerId var1) throws OperatorCreationException;
}
