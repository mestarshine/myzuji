package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERExternal extends ASN1Primitive {
    private ASN1ObjectIdentifier directReference;
    private ASN1Integer indirectReference;
    private ASN1Primitive dataValueDescriptor;
    private int encoding;
    private ASN1Primitive externalContent;

    public DERExternal(ASN1EncodableVector vector) {
        int offset = 0;
        ASN1Primitive enc = this.getObjFromVector(vector, offset);
        if (enc instanceof ASN1ObjectIdentifier) {
            this.directReference = (ASN1ObjectIdentifier) enc;
            ++offset;
            enc = this.getObjFromVector(vector, offset);
        }

        if (enc instanceof ASN1Integer) {
            this.indirectReference = (ASN1Integer) enc;
            ++offset;
            enc = this.getObjFromVector(vector, offset);
        }

        if (!(enc instanceof DERTaggedObject)) {
            this.dataValueDescriptor = enc;
            ++offset;
            enc = this.getObjFromVector(vector, offset);
        }

        if (vector.size() != offset + 1) {
            throw new IllegalArgumentException("input vector too large");
        } else if (!(enc instanceof DERTaggedObject)) {
            throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
        } else {
            DERTaggedObject obj = (DERTaggedObject) enc;
            this.setEncoding(obj.getTagNo());
            this.externalContent = obj.getObject();
        }
    }

    private ASN1Primitive getObjFromVector(ASN1EncodableVector v, int index) {
        if (v.size() <= index) {
            throw new IllegalArgumentException("too few objects in input vector");
        } else {
            return v.get(index).toASN1Primitive();
        }
    }

    public DERExternal(ASN1ObjectIdentifier directReference, ASN1Integer indirectReference, ASN1Primitive dataValueDescriptor, DERTaggedObject externalData) {
        this(directReference, indirectReference, dataValueDescriptor, externalData.getTagNo(), externalData.toASN1Primitive());
    }

    public DERExternal(ASN1ObjectIdentifier directReference, ASN1Integer indirectReference, ASN1Primitive dataValueDescriptor, int encoding, ASN1Primitive externalData) {
        this.setDirectReference(directReference);
        this.setIndirectReference(indirectReference);
        this.setDataValueDescriptor(dataValueDescriptor);
        this.setEncoding(encoding);
        this.setExternalContent(externalData.toASN1Primitive());
    }

    public int hashCode() {
        int ret = 0;
        if (this.directReference != null) {
            ret = this.directReference.hashCode();
        }

        if (this.indirectReference != null) {
            ret ^= this.indirectReference.hashCode();
        }

        if (this.dataValueDescriptor != null) {
            ret ^= this.dataValueDescriptor.hashCode();
        }

        ret ^= this.externalContent.hashCode();
        return ret;
    }

    public boolean isConstructed() {
        return true;
    }

    public int encodedLength() throws IOException {
        return this.getEncoded().length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (this.directReference != null) {
            baos.write(this.directReference.getEncoded("DER"));
        }

        if (this.indirectReference != null) {
            baos.write(this.indirectReference.getEncoded("DER"));
        }

        if (this.dataValueDescriptor != null) {
            baos.write(this.dataValueDescriptor.getEncoded("DER"));
        }

        DERTaggedObject obj = new DERTaggedObject(true, this.encoding, this.externalContent);
        baos.write(obj.getEncoded("DER"));
        out.writeEncoded(32, 8, baos.toByteArray());
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof DERExternal)) {
            return false;
        } else if (this == o) {
            return true;
        } else {
            DERExternal other = (DERExternal) o;
            if (this.directReference != null && (other.directReference == null || !other.directReference.equals(this.directReference))) {
                return false;
            } else if (this.indirectReference == null || other.indirectReference != null && other.indirectReference.equals(this.indirectReference)) {
                return this.dataValueDescriptor == null || other.dataValueDescriptor != null && other.dataValueDescriptor.equals(this.dataValueDescriptor) ? this.externalContent.equals(other.externalContent) : false;
            } else {
                return false;
            }
        }
    }

    public ASN1Primitive getDataValueDescriptor() {
        return this.dataValueDescriptor;
    }

    public ASN1ObjectIdentifier getDirectReference() {
        return this.directReference;
    }

    public int getEncoding() {
        return this.encoding;
    }

    public ASN1Primitive getExternalContent() {
        return this.externalContent;
    }

    public ASN1Integer getIndirectReference() {
        return this.indirectReference;
    }

    private void setDataValueDescriptor(ASN1Primitive dataValueDescriptor) {
        this.dataValueDescriptor = dataValueDescriptor;
    }

    private void setDirectReference(ASN1ObjectIdentifier directReferemce) {
        this.directReference = directReferemce;
    }

    private void setEncoding(int encoding) {
        if (encoding >= 0 && encoding <= 2) {
            this.encoding = encoding;
        } else {
            throw new IllegalArgumentException("invalid encoding value: " + encoding);
        }
    }

    private void setExternalContent(ASN1Primitive externalContent) {
        this.externalContent = externalContent;
    }

    private void setIndirectReference(ASN1Integer indirectReference) {
        this.indirectReference = indirectReference;
    }
}
