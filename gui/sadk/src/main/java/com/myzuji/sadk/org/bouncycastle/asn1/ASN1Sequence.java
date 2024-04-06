package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends ASN1Primitive {
    protected Vector seq = new Vector();

    protected ASN1Sequence() {
    }

    protected ASN1Sequence(ASN1Encodable obj) {
        this.seq.addElement(obj);
    }

    protected ASN1Sequence(ASN1EncodableVector v) {
        for (int i = 0; i != v.size(); ++i) {
            this.seq.addElement(v.get(i));
        }

    }

    protected ASN1Sequence(ASN1Encodable[] array) {
        for (int i = 0; i != array.length; ++i) {
            this.seq.addElement(array[i]);
        }

    }

    public static ASN1Sequence getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1Sequence)) {
            if (obj instanceof ASN1SequenceParser) {
                return getInstance(((ASN1SequenceParser) obj).toASN1Primitive());
            } else if (obj instanceof byte[]) {
                try {
                    return getInstance(fromByteArray((byte[]) ((byte[]) obj)));
                } catch (IOException var2) {
                    IOException e = var2;
                    throw new IllegalArgumentException("failed to construct sequence from byte[]: " + e.getMessage());
                }
            } else {
                if (obj instanceof ASN1Encodable) {
                    ASN1Primitive primitive = ((ASN1Encodable) obj).toASN1Primitive();
                    if (primitive instanceof ASN1Sequence) {
                        return (ASN1Sequence) primitive;
                    }
                }

                throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1Sequence) obj;
        }
    }

    public static ASN1Sequence getInstance(ASN1TaggedObject obj, boolean explicit) {
        if (explicit) {
            if (!obj.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            } else {
                return getInstance(obj.getObject().toASN1Primitive());
            }
        } else if (obj.isExplicit()) {
            return (ASN1Sequence) (obj instanceof BERTaggedObject ? new BERSequence(obj.getObject()) : new DLSequence(obj.getObject()));
        } else if (obj.getObject() instanceof ASN1Sequence) {
            return (ASN1Sequence) obj.getObject();
        } else {
            throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
        }
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] values = new ASN1Encodable[this.size()];

        for (int i = 0; i != this.size(); ++i) {
            values[i] = this.getObjectAt(i);
        }

        return values;
    }

    public Enumeration getObjects() {
        return this.seq.elements();
    }

    public ASN1SequenceParser parser() {
        final ASN1Sequence outer = this;
        return new ASN1SequenceParser() {
            private final int max = ASN1Sequence.this.size();
            private int index;

            public ASN1Encodable readObject() throws IOException {
                if (this.index == this.max) {
                    return null;
                } else {
                    ASN1Encodable obj = ASN1Sequence.this.getObjectAt(this.index++);
                    if (obj instanceof ASN1Sequence) {
                        return ((ASN1Sequence) obj).parser();
                    } else {
                        return (ASN1Encodable) (obj instanceof ASN1Set ? ((ASN1Set) obj).parser() : obj);
                    }
                }
            }

            public ASN1Primitive getLoadedObject() {
                return outer;
            }

            public ASN1Primitive toASN1Primitive() {
                return outer;
            }
        };
    }

    public ASN1Encodable getObjectAt(int index) {
        return (ASN1Encodable) this.seq.elementAt(index);
    }

    public int size() {
        return this.seq.size();
    }

    public int hashCode() {
        Enumeration e = this.getObjects();

        int hashCode;
        ASN1Encodable o;
        for (hashCode = this.size(); e.hasMoreElements(); hashCode ^= o.hashCode()) {
            o = this.getNext(e);
            hashCode *= 17;
        }

        return hashCode;
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1Sequence)) {
            return false;
        } else {
            ASN1Sequence other = (ASN1Sequence) o;
            if (this.size() != other.size()) {
                return false;
            } else {
                Enumeration s1 = this.getObjects();
                Enumeration s2 = other.getObjects();

                ASN1Primitive o1;
                ASN1Primitive o2;
                do {
                    if (!s1.hasMoreElements()) {
                        return true;
                    }

                    ASN1Encodable obj1 = this.getNext(s1);
                    ASN1Encodable obj2 = this.getNext(s2);
                    o1 = obj1.toASN1Primitive();
                    o2 = obj2.toASN1Primitive();
                } while (o1 == o2 || o1.equals(o2));

                return false;
            }
        }
    }

    private ASN1Encodable getNext(Enumeration e) {
        ASN1Encodable encObj = (ASN1Encodable) e.nextElement();
        return encObj;
    }

    public ASN1Primitive toDERObject() {
        ASN1Sequence derSeq = new DERSequence();
        derSeq.seq = this.seq;
        return derSeq;
    }

    public ASN1Primitive toDLObject() {
        ASN1Sequence dlSeq = new DLSequence();
        dlSeq.seq = this.seq;
        return dlSeq;
    }

    public boolean isConstructed() {
        return true;
    }

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public String toString() {
        return this.seq.toString();
    }
}
