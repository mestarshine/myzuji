package com.myzuji.sadk.org.bouncycastle.operator.bc;

import com.myzuji.sadk.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.myzuji.sadk.org.bouncycastle.crypto.Digest;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculator;
import com.myzuji.sadk.org.bouncycastle.operator.DigestCalculatorProvider;
import com.myzuji.sadk.org.bouncycastle.operator.OperatorCreationException;

import java.io.IOException;
import java.io.OutputStream;

public class BcDigestCalculatorProvider implements DigestCalculatorProvider {
    private BcDigestProvider digestProvider;

    public BcDigestCalculatorProvider() {
        this.digestProvider = BcDefaultDigestProvider.INSTANCE;
    }

    public DigestCalculator get(final AlgorithmIdentifier algorithm) throws OperatorCreationException {
        Digest dig = this.digestProvider.get(algorithm);
        final DigestOutputStream stream = new DigestOutputStream(dig);
        return new DigestCalculator() {
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return algorithm;
            }

            public OutputStream getOutputStream() {
                return stream;
            }

            public byte[] getDigest() {
                return stream.getDigest();
            }
        };
    }

    private class DigestOutputStream extends OutputStream {
        private Digest dig;

        DigestOutputStream(Digest dig) {
            this.dig = dig;
        }

        public void write(byte[] bytes, int off, int len) throws IOException {
            this.dig.update(bytes, off, len);
        }

        public void write(byte[] bytes) throws IOException {
            this.dig.update(bytes, 0, bytes.length);
        }

        public void write(int b) throws IOException {
            this.dig.update((byte) b);
        }

        byte[] getDigest() {
            byte[] d = new byte[this.dig.getDigestSize()];
            this.dig.doFinal(d, 0);
            return d;
        }
    }
}
