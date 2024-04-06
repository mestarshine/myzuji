package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends ASN1Primitive implements ASN1TaggedObjectParser {
    int tagNo;
    boolean empty = false;
    boolean explicit = true;
    ASN1Encodable obj = null;

    public static ASN1TaggedObject getInstance(ASN1TaggedObject obj, boolean explicit) {
        if (explicit) {
            return (ASN1TaggedObject) obj.getObject();
        } else {
            throw new IllegalArgumentException("implicitly tagged tagged object");
        }
    }

    public static ASN1TaggedObject getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1TaggedObject)) {
            if (obj instanceof byte[]) {
                try {
                    return getInstance(fromByteArray((byte[]) ((byte[]) obj)));
                } catch (IOException var2) {
                    IOException e = var2;
                    throw new IllegalArgumentException("failed to construct tagged object from byte[]: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1TaggedObject) obj;
        }
    }

    public ASN1TaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
        if (obj instanceof ASN1Choice) {
            this.explicit = true;
        } else {
            this.explicit = explicit;
        }

        this.tagNo = tagNo;
        if (this.explicit) {
            this.obj = obj;
        } else {
            obj.toASN1Primitive();
            this.obj = obj;
        }

    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1TaggedObject)) {
            return false;
        } else {
            ASN1TaggedObject other = (ASN1TaggedObject) o;
            if (this.tagNo == other.tagNo && this.empty == other.empty && this.explicit == other.explicit) {
                if (this.obj == null) {
                    if (other.obj != null) {
                        return false;
                    }
                } else if (!this.obj.toASN1Primitive().equals(other.obj.toASN1Primitive())) {
                    return false;
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public int hashCode() {
        int code = this.tagNo;
        if (this.obj != null) {
            code ^= this.obj.hashCode();
        }

        return code;
    }

    public int getTagNo() {
        return this.tagNo;
    }

    public boolean isExplicit() {
        return this.explicit;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public ASN1Primitive getObject() {
        return this.obj != null ? this.obj.toASN1Primitive() : null;
    }

    public ASN1Encodable getObjectParser(int tag, boolean isExplicit) {
        switch (tag) {
            case 4:
                return ASN1OctetString.getInstance(this, isExplicit).parser();
            case 16:
                return ASN1Sequence.getInstance(this, isExplicit).parser();
            case 17:
                return ASN1Set.getInstance(this, isExplicit).parser();
            default:
                if (isExplicit) {
                    return this.getObject();
                } else {
                    throw new RuntimeException("implicit tagging not implemented for tag: " + tag);
                }
        }
    }

    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }

    public ASN1Primitive toDERObject() {
        return new DERTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    public ASN1Primitive toDLObject() {
        return new DLTaggedObject(this.explicit, this.tagNo, this.obj);
    }

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public String toString() {
        return "[" + this.tagNo + "]" + this.obj;
    }
}
