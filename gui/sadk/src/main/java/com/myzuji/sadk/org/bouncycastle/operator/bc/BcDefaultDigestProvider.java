package com.myzuji.sadk.org.bouncycastle.operator.bc;

import com.myzuji.sadk.org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.crypto.digests.*;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;
import com.myzuji.sadk.org.bouncycastle.pkcs.PKCSObjectIdentifiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BcDefaultDigestProvider implements BcDigestProvider {
    private static final Map lookup = createTable();
    public static final BcDigestProvider INSTANCE = new BcDefaultDigestProvider();

    private static Map createTable() {
        Map table = new HashMap();
        table.put(OIWObjectIdentifiers.idSHA1, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new SHA1Digest();
            }
        });
        table.put(NISTObjectIdentifiers.id_sha224, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new SHA224Digest();
            }
        });
        table.put(NISTObjectIdentifiers.id_sha256, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new SHA256Digest();
            }
        });
        table.put(NISTObjectIdentifiers.id_sha384, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new SHA384Digest();
            }
        });
        table.put(NISTObjectIdentifiers.id_sha512, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new SHA512Digest();
            }
        });
        table.put(PKCSObjectIdentifiers.md5, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new MD5Digest();
            }
        });
        table.put(PKCSObjectIdentifiers.md4, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new MD4Digest();
            }
        });
        table.put(PKCSObjectIdentifiers.md2, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new MD2Digest();
            }
        });
        table.put(CryptoProObjectIdentifiers.gostR3411, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new GOST3411Digest();
            }
        });
        table.put(TeleTrusTObjectIdentifiers.ripemd128, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new RIPEMD128Digest();
            }
        });
        table.put(TeleTrusTObjectIdentifiers.ripemd160, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new RIPEMD160Digest();
            }
        });
        table.put(TeleTrusTObjectIdentifiers.ripemd256, new BcDigestProvider() {
            public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) {
                return new RIPEMD256Digest();
            }
        });
        return Collections.unmodifiableMap(table);
    }

    private BcDefaultDigestProvider() {
    }

    public ExtendedDigest get(AlgorithmIdentifier digestAlgorithmIdentifier) throws OperatorCreationException {
        BcDigestProvider extProv = (BcDigestProvider) lookup.get(digestAlgorithmIdentifier.getAlgorithm());
        if (extProv == null) {
            throw new OperatorCreationException("cannot recognise digest");
        } else {
            return extProv.get(digestAlgorithmIdentifier);
        }
    }
}
