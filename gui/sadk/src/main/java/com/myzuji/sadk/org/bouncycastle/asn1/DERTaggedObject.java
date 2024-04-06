package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject {

    private static final byte[] ZERO_BYTES = new byte[0];

    public DERTaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
        super(explicit, tagNo, obj);
    }

    public DERTaggedObject(int tagNo, ASN1Encodable encodable) {
        super(true, tagNo, encodable);
    }

    public boolean isConstructed() {
        if (!this.empty) {
            if (this.explicit) {
                return true;
            } else {
                ASN1Primitive primitive = this.obj.toASN1Primitive().toDERObject();
                return primitive.isConstructed();
            }
        } else {
            return true;
        }
    }

    public int encodedLength() throws IOException {
        if (!this.empty) {
            ASN1Primitive primitive = this.obj.toASN1Primitive().toDERObject();
            int length = primitive.encodedLength();
            if (this.explicit) {
                return StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(length) + length;
            } else {
                --length;
                return StreamUtil.calculateTagLength(this.tagNo) + length;
            }
        } else {
            return StreamUtil.calculateTagLength(this.tagNo) + 1;
        }
    }

    public void encode(ASN1OutputStream out) throws IOException {
        if (!this.empty) {
            ASN1Primitive primitive = this.obj.toASN1Primitive().toDERObject();
            if (this.explicit) {
                out.writeTag(160, this.tagNo);
                out.writeLength(primitive.encodedLength());
                out.writeObject(primitive);
            } else {
                short flags;
                if (primitive.isConstructed()) {
                    flags = 160;
                } else {
                    flags = 128;
                }

                out.writeTag(flags, this.tagNo);
                out.writeImplicitObject(primitive);
            }
        } else {
            out.writeEncoded(160, this.tagNo, ZERO_BYTES);
        }

    }
}
