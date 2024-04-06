package com.myzuji.sadk.org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BEROctetString extends ASN1OctetString {
    private static final int MAX_LENGTH = 1000;
    private ASN1OctetString[] octs;

    private static byte[] toBytes(ASN1OctetString[] octs) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        for (int i = 0; i != octs.length; ++i) {
            try {
                DEROctetString o = (DEROctetString) octs[i];
                bOut.write(o.getOctets());
            } catch (ClassCastException var4) {
                throw new IllegalArgumentException(octs[i].getClass().getName() + " found in input should only contain DEROctetString");
            } catch (IOException var5) {
                IOException e = var5;
                throw new IllegalArgumentException("exception converting octets " + e.toString());
            }
        }

        return bOut.toByteArray();
    }

    public BEROctetString(byte[] string) {
        super(string);
    }

    public BEROctetString(ASN1OctetString[] octs) {
        super(toBytes(octs));
        this.octs = octs;
    }

    public byte[] getOctets() {
        return this.string;
    }

    public Enumeration getObjects() {
        return this.octs == null ? this.generateOcts().elements() : new Enumeration() {
            int counter = 0;

            public boolean hasMoreElements() {
                return this.counter < BEROctetString.this.octs.length;
            }

            public Object nextElement() {
                return BEROctetString.this.octs[this.counter++];
            }
        };
    }

    private Vector generateOcts() {
        Vector vec = new Vector();

        for (int i = 0; i < this.string.length; i += 1000) {
            int end;
            if (i + 1000 > this.string.length) {
                end = this.string.length;
            } else {
                end = i + 1000;
            }

            byte[] nStr = new byte[end - i];
            System.arraycopy(this.string, i, nStr, 0, nStr.length);
            vec.addElement(new DEROctetString(nStr));
        }

        return vec;
    }

    public boolean isConstructed() {
        return true;
    }

    public int encodedLength() throws IOException {
        int length = 0;

        for (Enumeration e = this.getObjects(); e.hasMoreElements(); length += ((ASN1Encodable) e.nextElement()).toASN1Primitive().encodedLength()) {
        }

        return 2 + length + 2;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.write(36);
        out.write(128);
        Enumeration e = this.getObjects();

        while (e.hasMoreElements()) {
            out.writeObject((ASN1Encodable) e.nextElement());
        }

        out.write(0);
        out.write(0);
    }

    static BEROctetString fromSequence(ASN1Sequence seq) {
        ASN1OctetString[] v = new ASN1OctetString[seq.size()];
        Enumeration e = seq.getObjects();

        for (int index = 0; e.hasMoreElements(); v[index++] = (ASN1OctetString) e.nextElement()) {
        }

        return new BEROctetString(v);
    }
}
