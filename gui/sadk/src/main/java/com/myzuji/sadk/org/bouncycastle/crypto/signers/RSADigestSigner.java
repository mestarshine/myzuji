package com.myzuji.sadk.org.bouncycastle.crypto.signers;

import com.myzuji.sadk.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.DERNull;
import com.myzuji.sadk.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.DigestInfo;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.crypto.*;
import com.myzuji.sadk.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import com.myzuji.sadk.org.bouncycastle.crypto.engines.RSABlindedEngine;
import com.myzuji.sadk.org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import com.myzuji.sadk.org.bouncycastle.crypto.params.ParametersWithRandom;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.IOException;
import java.util.Hashtable;

public class RSADigestSigner implements Signer {
    private final AsymmetricBlockCipher rsaEngine;
    private final AlgorithmIdentifier algId;
    private final Digest digest;
    private boolean forSigning;
    private static final Hashtable oidMap = new Hashtable();

    public RSADigestSigner(Digest digest) {
        this(digest, (ASN1ObjectIdentifier) oidMap.get(digest.getAlgorithmName()));
    }

    public RSADigestSigner(Digest digest, ASN1ObjectIdentifier digestOid) {
        this.rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
        this.digest = digest;
        this.algId = new AlgorithmIdentifier(digestOid, DERNull.INSTANCE);
    }


    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "withRSA";
    }

    public void init(boolean forSigning, CipherParameters parameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter k;
        if (parameters instanceof ParametersWithRandom) {
            k = (AsymmetricKeyParameter) ((ParametersWithRandom) parameters).getParameters();
        } else {
            k = (AsymmetricKeyParameter) parameters;
        }

        if (forSigning && !k.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        } else if (!forSigning && k.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        } else {
            this.reset();
            this.rsaEngine.init(forSigning, parameters);
        }
    }

    public void update(byte input) {
        this.digest.update(input);
    }

    public void update(byte[] input, int inOff, int length) {
        this.digest.update(input, inOff, length);
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (!this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for signature generation.");
        } else {
            byte[] hash = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(hash, 0);

            try {
                byte[] data = this.derEncode(hash);
                return this.rsaEngine.processBlock(data, 0, data.length);
            } catch (IOException var3) {
                IOException e = var3;
                throw new CryptoException("unable to encode signature: " + e.getMessage(), e);
            }
        }
    }

    public boolean verifySignature(byte[] signature) {
        if (this.forSigning) {
            throw new IllegalStateException("RSADigestSigner not initialised for verification");
        } else {
            byte[] hash = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(hash, 0);

            byte[] sig;
            byte[] expected;
            try {
                sig = this.rsaEngine.processBlock(signature, 0, signature.length);
                expected = this.derEncode(hash);
            } catch (Exception var9) {
                return false;
            }

            if (sig.length == expected.length) {
                return Arrays.constantTimeAreEqual(sig, expected);
            } else if (sig.length != expected.length - 2) {
                return false;
            } else {
                int sigOffset = sig.length - hash.length - 2;
                int expectedOffset = expected.length - hash.length - 2;
                expected[1] = (byte) (expected[1] - 2);
                expected[3] = (byte) (expected[3] - 2);
                int nonEqual = 0;

                int i;
                for (i = 0; i < hash.length; ++i) {
                    nonEqual |= sig[sigOffset + i] ^ expected[expectedOffset + i];
                }

                for (i = 0; i < sigOffset; ++i) {
                    nonEqual |= sig[i] ^ expected[i];
                }

                return nonEqual == 0;
            }
        }
    }

    public void reset() {
        this.digest.reset();
    }

    private byte[] derEncode(byte[] hash) throws IOException {
        DigestInfo dInfo = new DigestInfo(this.algId, hash);
        return dInfo.getEncoded("DER");
    }

    static {
        oidMap.put("RIPEMD128", TeleTrusTObjectIdentifiers.ripemd128);
        oidMap.put("RIPEMD160", TeleTrusTObjectIdentifiers.ripemd160);
        oidMap.put("RIPEMD256", TeleTrusTObjectIdentifiers.ripemd256);
        oidMap.put("SHA-1", X509ObjectIdentifiers.id_SHA1);
        oidMap.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        oidMap.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        oidMap.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        oidMap.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        oidMap.put("SHA-512/224", NISTObjectIdentifiers.id_sha512_224);
        oidMap.put("SHA-512/256", NISTObjectIdentifiers.id_sha512_256);
        oidMap.put("MD2", PKCSObjectIdentifiers.md2);
        oidMap.put("MD4", PKCSObjectIdentifiers.md4);
        oidMap.put("MD5", PKCSObjectIdentifiers.md5);
    }
}
