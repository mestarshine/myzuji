package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends ASN1TaggedObject {
    public BERTaggedObject(int tagNo, ASN1Encodable obj) {
        super(true, tagNo, obj);
    }

    public BERTaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
        super(explicit, tagNo, obj);
    }

    public BERTaggedObject(int tagNo) {
        super(false, tagNo, new BERSequence());
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
            ASN1Primitive primitive = this.obj.toASN1Primitive();
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
        out.writeTag(160, this.tagNo);
        out.write(128);
        if (!this.empty) {
            if (!this.explicit) {
                Enumeration e;
                if (this.obj instanceof ASN1OctetString) {
                    if (this.obj instanceof BEROctetString) {
                        e = ((BEROctetString) this.obj).getObjects();
                    } else {
                        ASN1OctetString octs = (ASN1OctetString) this.obj;
                        BEROctetString berO = new BEROctetString(octs.getOctets());
                        e = berO.getObjects();
                    }
                } else if (this.obj instanceof ASN1Sequence) {
                    e = ((ASN1Sequence) this.obj).getObjects();
                } else {
                    if (!(this.obj instanceof ASN1Set)) {
                        throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
                    }

                    e = ((ASN1Set) this.obj).getObjects();
                }

                while (e.hasMoreElements()) {
                    out.writeObject((ASN1Encodable) e.nextElement());
                }
            } else {
                out.writeObject(this.obj);
            }
        }

        out.write(0);
        out.write(0);
    }
}
