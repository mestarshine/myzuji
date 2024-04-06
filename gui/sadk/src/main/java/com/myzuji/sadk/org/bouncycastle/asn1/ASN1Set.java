package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Set extends ASN1Primitive {

    private Vector set = new Vector();
    private boolean isSorted = false;

    public static ASN1Set getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1Set)) {
            if (obj instanceof ASN1SetParser) {
                return getInstance(((ASN1SetParser) obj).toASN1Primitive());
            } else if (obj instanceof byte[]) {
                try {
                    return getInstance(fromByteArray((byte[]) ((byte[]) obj)));
                } catch (IOException var2) {
                    IOException e = var2;
                    throw new IllegalArgumentException("failed to construct set from byte[]: " + e.getMessage());
                }
            } else {
                if (obj instanceof ASN1Encodable) {
                    ASN1Primitive primitive = ((ASN1Encodable) obj).toASN1Primitive();
                    if (primitive instanceof ASN1Set) {
                        return (ASN1Set) primitive;
                    }
                }

                throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1Set) obj;
        }
    }

    public static ASN1Set getInstance(ASN1TaggedObject obj, boolean explicit) {
        if (explicit) {
            if (!obj.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            } else {
                return (ASN1Set) obj.getObject();
            }
        } else if (obj.isExplicit()) {
            return (ASN1Set) (obj instanceof BERTaggedObject ? new BERSet(obj.getObject()) : new DLSet(obj.getObject()));
        } else if (obj.getObject() instanceof ASN1Set) {
            return (ASN1Set) obj.getObject();
        } else if (obj.getObject() instanceof ASN1Sequence) {
            ASN1Sequence s = (ASN1Sequence) obj.getObject();
            return (ASN1Set) (obj instanceof BERTaggedObject ? new BERSet(s.toArray()) : new DLSet(s.toArray()));
        } else {
            throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
        }
    }

    protected ASN1Set() {
    }

    protected ASN1Set(ASN1Encodable obj) {
        this.set.addElement(obj);
    }

    protected ASN1Set(ASN1EncodableVector v, boolean doSort) {
        for (int i = 0; i != v.size(); ++i) {
            this.set.addElement(v.get(i));
        }

        if (doSort) {
            this.sort();
        }

    }

    protected ASN1Set(ASN1Encodable[] array, boolean doSort) {
        for (int i = 0; i != array.length; ++i) {
            this.set.addElement(array[i]);
        }

        if (doSort) {
            this.sort();
        }

    }

    public Enumeration getObjects() {
        return this.set.elements();
    }

    public ASN1Encodable getObjectAt(int index) {
        return (ASN1Encodable) this.set.elementAt(index);
    }

    public int size() {
        return this.set.size();
    }

    public ASN1Encodable[] toArray() {
        ASN1Encodable[] values = new ASN1Encodable[this.size()];

        for (int i = 0; i != this.size(); ++i) {
            values[i] = this.getObjectAt(i);
        }

        return values;
    }

    public ASN1SetParser parser() {
        final ASN1Set outer = this;
        return new ASN1SetParser() {
            private final int max = ASN1Set.this.size();
            private int index;

            public ASN1Encodable readObject() throws IOException {
                if (this.index == this.max) {
                    return null;
                } else {
                    ASN1Encodable obj = ASN1Set.this.getObjectAt(this.index++);
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

    public ASN1Primitive toDERObject() {
        if (this.isSorted) {
            ASN1Set derSet = new DERSet();
            derSet.set = this.set;
            return derSet;
        } else {
            Vector v = new Vector();

            for (int i = 0; i != this.set.size(); ++i) {
                v.addElement(this.set.elementAt(i));
            }

            ASN1Set derSet = new DERSet();
            derSet.set = v;
            derSet.sort();
            return derSet;
        }
    }

    public ASN1Primitive toDLObject() {
        ASN1Set derSet = new DLSet();
        derSet.set = this.set;
        return derSet;
    }

    public boolean asn1Equals(ASN1Primitive o) {
        if (!(o instanceof ASN1Set)) {
            return false;
        } else {
            ASN1Set other = (ASN1Set) o;
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
        return (ASN1Encodable) (encObj == null ? DERNull.INSTANCE : encObj);
    }

    private boolean lessThanOrEqual(byte[] a, byte[] b) {
        int len = Math.min(a.length, b.length);

        for (int i = 0; i != len; ++i) {
            if (a[i] != b[i]) {
                return (a[i] & 255) < (b[i] & 255);
            }
        }

        return len == a.length;
    }

    private byte[] getEncoded(ASN1Encodable obj) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ASN1OutputStream aOut = new ASN1OutputStream(bOut);

        try {
            aOut.writeObject(obj);
        } catch (IOException var5) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }

        return bOut.toByteArray();
    }

    protected void sort() {
        if (!this.isSorted) {
            this.isSorted = true;
            if (this.set.size() > 1) {
                boolean swapped = true;

                int swapIndex;
                for (int lastSwap = this.set.size() - 1; swapped; lastSwap = swapIndex) {
                    int index = 0;
                    swapIndex = 0;
                    byte[] a = this.getEncoded((ASN1Encodable) this.set.elementAt(0));

                    for (swapped = false; index != lastSwap; ++index) {
                        byte[] b = this.getEncoded((ASN1Encodable) this.set.elementAt(index + 1));
                        if (this.lessThanOrEqual(a, b)) {
                            a = b;
                        } else {
                            Object o = this.set.elementAt(index);
                            this.set.setElementAt(this.set.elementAt(index + 1), index);
                            this.set.setElementAt(o, index + 1);
                            swapped = true;
                            swapIndex = index;
                        }
                    }
                }
            }
        }

    }

    public boolean isConstructed() {
        return true;
    }

    public abstract void encode(ASN1OutputStream var1) throws IOException;

    public String toString() {
        return this.set.toString();
    }
}
