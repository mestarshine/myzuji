package com.myzuji.sadk.org.bouncycastle.cms;

import com.myzuji.sadk.org.bouncycastle.asn1.*;

import java.io.IOException;

public class SignedDataParser {
    private ASN1SequenceParser _seq;
    private ASN1Integer _version;
    private Object _nextObject;
    private boolean _certsCalled;
    private boolean _crlsCalled;

    public static SignedDataParser getInstance(Object o) throws IOException {
        if (o instanceof ASN1Sequence) {
            return new SignedDataParser(((ASN1Sequence) o).parser());
        } else if (o instanceof ASN1SequenceParser) {
            return new SignedDataParser((ASN1SequenceParser) o);
        } else {
            throw new IOException("unknown object encountered: " + o.getClass().getName());
        }
    }

    private SignedDataParser(ASN1SequenceParser seq) throws IOException {
        this._seq = seq;
        this._version = (ASN1Integer) seq.readObject();
    }

    public ASN1Integer getVersion() {
        return this._version;
    }

    public ASN1SetParser getDigestAlgorithms() throws IOException {
        Object o = this._seq.readObject();
        return o instanceof ASN1Set ? ((ASN1Set) o).parser() : (ASN1SetParser) o;
    }

    public ContentInfoParser getEncapContentInfo() throws IOException {
        return new ContentInfoParser((ASN1SequenceParser) this._seq.readObject());
    }

    public ASN1SetParser getCertificates() throws IOException {
        this._certsCalled = true;
        this._nextObject = this._seq.readObject();
        if (this._nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser) this._nextObject).getTagNo() == 0) {
            ASN1SetParser certs = (ASN1SetParser) ((ASN1TaggedObjectParser) this._nextObject).getObjectParser(17, false);
            this._nextObject = null;
            return certs;
        } else {
            return null;
        }
    }

    public ASN1SetParser getCrls() throws IOException {
        if (!this._certsCalled) {
            throw new IOException("getCerts() has not been called.");
        } else {
            this._crlsCalled = true;
            if (this._nextObject == null) {
                this._nextObject = this._seq.readObject();
            }

            if (this._nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser) this._nextObject).getTagNo() == 1) {
                ASN1SetParser crls = (ASN1SetParser) ((ASN1TaggedObjectParser) this._nextObject).getObjectParser(17, false);
                this._nextObject = null;
                return crls;
            } else {
                return null;
            }
        }
    }

    public ASN1SetParser getSignerInfos() throws IOException {
        if (this._certsCalled && this._crlsCalled) {
            if (this._nextObject == null) {
                this._nextObject = this._seq.readObject();
            }

            return (ASN1SetParser) this._nextObject;
        } else {
            throw new IOException("getCerts() and/or getCrls() has not been called.");
        }
    }
}
