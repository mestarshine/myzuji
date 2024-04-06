package com.myzuji.sadk.org.bouncycastle.asn1.x509;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.util.Strings;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class X509Name extends ASN1Object {

    public static final ASN1ObjectIdentifier C = new ASN1ObjectIdentifier("2.5.4.6");

    public static final ASN1ObjectIdentifier O = new ASN1ObjectIdentifier("2.5.4.10");

    public static final ASN1ObjectIdentifier OU = new ASN1ObjectIdentifier("2.5.4.11");

    public static final ASN1ObjectIdentifier T = new ASN1ObjectIdentifier("2.5.4.12");

    public static final ASN1ObjectIdentifier CN = new ASN1ObjectIdentifier("2.5.4.3");
    public static final ASN1ObjectIdentifier SN = new ASN1ObjectIdentifier("2.5.4.5");
    public static final ASN1ObjectIdentifier STREET = new ASN1ObjectIdentifier("2.5.4.9");
    public static final ASN1ObjectIdentifier SERIALNUMBER;
    public static final ASN1ObjectIdentifier L;
    public static final ASN1ObjectIdentifier ST;
    public static final ASN1ObjectIdentifier SURNAME;
    public static final ASN1ObjectIdentifier GIVENNAME;
    public static final ASN1ObjectIdentifier INITIALS;
    public static final ASN1ObjectIdentifier GENERATION;
    public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER;
    public static final ASN1ObjectIdentifier BUSINESS_CATEGORY;
    public static final ASN1ObjectIdentifier POSTAL_CODE;
    public static final ASN1ObjectIdentifier DN_QUALIFIER;
    public static final ASN1ObjectIdentifier PSEUDONYM;
    public static final ASN1ObjectIdentifier DATE_OF_BIRTH;
    public static final ASN1ObjectIdentifier PLACE_OF_BIRTH;
    public static final ASN1ObjectIdentifier GENDER;
    public static final ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final ASN1ObjectIdentifier NAME_AT_BIRTH;
    public static final ASN1ObjectIdentifier POSTAL_ADDRESS;
    public static final ASN1ObjectIdentifier DMD_NAME;
    public static final ASN1ObjectIdentifier TELEPHONE_NUMBER;
    public static final ASN1ObjectIdentifier NAME;

    public static final ASN1ObjectIdentifier EmailAddress;
    public static final ASN1ObjectIdentifier UnstructuredName;
    public static final ASN1ObjectIdentifier UnstructuredAddress;
    public static final ASN1ObjectIdentifier E;
    public static final ASN1ObjectIdentifier DC;
    public static final ASN1ObjectIdentifier UID;
    public static boolean DefaultReverse;
    public static final Hashtable DefaultSymbols;
    public static final Hashtable RFC2253Symbols;
    public static final Hashtable RFC1779Symbols;
    public static final Hashtable DefaultLookUp;

    public static final Hashtable OIDLookUp;

    public static final Hashtable SymbolLookUp;
    private static final Boolean TRUE;
    private static final Boolean FALSE;
    private X509NameEntryConverter converter;
    private Vector ordering;
    private Vector values;
    private Vector added;
    private ASN1Sequence seq;
    private boolean isHashCodeCalculated;
    private int hashCodeValue;

    public static X509Name getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static X509Name getInstance(Object obj) {
        X509Name result = null;
        if (obj != null && !(obj instanceof X509Name)) {
            if (obj instanceof X500Name) {
                result = new X509Name(ASN1Sequence.getInstance(((X500Name) obj).toASN1Primitive()));
            } else if (obj != null) {
                result = new X509Name(ASN1Sequence.getInstance(obj));
            }
        } else {
            result = (X509Name) obj;
        }

        return result;
    }

    protected X509Name() {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
    }


    public X509Name(ASN1Sequence seq) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.seq = seq;
        Enumeration e = seq.getObjects();

        while (e.hasMoreElements()) {
            ASN1Set set = ASN1Set.getInstance(((ASN1Encodable) e.nextElement()).toASN1Primitive());

            for (int i = 0; i < set.size(); ++i) {
                ASN1Sequence s = ASN1Sequence.getInstance(set.getObjectAt(i).toASN1Primitive());
                if (s.size() != 2) {
                    throw new IllegalArgumentException("badly sized pair");
                }

                this.ordering.addElement(ASN1ObjectIdentifier.getInstance(s.getObjectAt(0)));
                ASN1Encodable value = s.getObjectAt(1);
                if (value instanceof ASN1String && !(value instanceof DERUniversalString)) {
                    String v = ((ASN1String) value).getString();
                    if (v.length() > 0 && v.charAt(0) == '#') {
                        this.values.addElement("\\" + v);
                    } else {
                        this.values.addElement(v);
                    }
                } else {
                    try {
                        this.values.addElement("#" + this.bytesToString(Hex.encode(value.toASN1Primitive().getEncoded("DER"))));
                    } catch (IOException var8) {
                        throw new IllegalArgumentException("cannot encode value");
                    }
                }

                this.added.addElement(i != 0 ? TRUE : FALSE);
            }
        }

    }


    public X509Name(Hashtable attributes) {
        this((Vector) null, (Hashtable) attributes);
    }

    public X509Name(Vector ordering, Hashtable attributes) {
        this(ordering, (Hashtable) attributes, new X509DefaultEntryConverter());
    }


    public X509Name(Vector ordering, Hashtable attributes, X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        int i;
        if (ordering != null) {
            for (i = 0; i != ordering.size(); ++i) {
                this.ordering.addElement(ordering.elementAt(i));
                this.added.addElement(FALSE);
            }
        } else {
            Enumeration e = attributes.keys();

            while (e.hasMoreElements()) {
                this.ordering.addElement(e.nextElement());
                this.added.addElement(FALSE);
            }
        }

        for (i = 0; i != this.ordering.size(); ++i) {
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
            if (attributes.get(oid) == null) {
                throw new IllegalArgumentException("No attribute for object id - " + oid.getId() + " - passed to distinguished name");
            }

            this.values.addElement(attributes.get(oid));
        }

    }


    public X509Name(Vector oids, Vector values) {
        this(oids, (Vector) values, new X509DefaultEntryConverter());
    }


    public X509Name(Vector oids, Vector values, X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        if (oids.size() != values.size()) {
            throw new IllegalArgumentException("oids vector must be same length as values.");
        } else {
            for (int i = 0; i < oids.size(); ++i) {
                this.ordering.addElement(oids.elementAt(i));
                this.values.addElement(values.elementAt(i));
                this.added.addElement(FALSE);
            }

        }
    }


    public X509Name(String dirName) {
        this(DefaultReverse, DefaultLookUp, dirName);
    }


    public X509Name(String dirName, X509NameEntryConverter converter) {
        this(DefaultReverse, DefaultLookUp, dirName, converter);
    }


    public X509Name(boolean reverse, String dirName) {
        this(reverse, DefaultLookUp, dirName);
    }


    public X509Name(boolean reverse, String dirName, X509NameEntryConverter converter) {
        this(reverse, DefaultLookUp, dirName, converter);
    }


    public X509Name(boolean reverse, Hashtable lookUp, String dirName) {
        this(reverse, lookUp, dirName, new X509DefaultEntryConverter());
    }

    private ASN1ObjectIdentifier decodeOID(String name, Hashtable lookUp) {
        name = name.trim();
        if (Strings.toUpperCase(name).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(name.substring(4));
        } else if (name.charAt(0) >= '0' && name.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(name);
        } else {
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) lookUp.get(Strings.toLowerCase(name));
            if (oid == null) {
                throw new IllegalArgumentException("Unknown object id - " + name + " - passed to distinguished name");
            } else {
                return oid;
            }
        }
    }

    private String unescape(String elt) {
        if (elt.length() != 0 && (elt.indexOf(92) >= 0 || elt.indexOf(34) >= 0)) {
            char[] elts = elt.toCharArray();
            boolean escaped = false;
            boolean quoted = false;
            StringBuffer buf = new StringBuffer(elt.length());
            int start = 0;
            if (elts[0] == '\\' && elts[1] == '#') {
                start = 2;
                buf.append("\\#");
            }

            boolean nonWhiteSpaceEncountered = false;
            int lastEscaped = 0;

            for (int i = start; i != elts.length; ++i) {
                char c = elts[i];
                if (c != ' ') {
                    nonWhiteSpaceEncountered = true;
                }

                if (c == '"') {
                    if (!escaped) {
                        quoted = !quoted;
                    } else {
                        buf.append(c);
                    }

                    escaped = false;
                } else if (c == '\\' && !escaped && !quoted) {
                    escaped = true;
                    lastEscaped = buf.length();
                } else if (c != ' ' || escaped || nonWhiteSpaceEncountered) {
                    buf.append(c);
                    escaped = false;
                }
            }

            if (buf.length() > 0) {
                while (buf.charAt(buf.length() - 1) == ' ' && lastEscaped != buf.length() - 1) {
                    buf.setLength(buf.length() - 1);
                }
            }

            return buf.toString();
        } else {
            return elt.trim();
        }
    }

    public X509Name(boolean reverse, Hashtable lookUp, String dirName, X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        X509NameTokenizer nTok = new X509NameTokenizer(dirName);

        while (true) {
            while (nTok.hasMoreTokens()) {
                String token = nTok.nextToken();
                if (token.indexOf(43) > 0) {
                    X509NameTokenizer pTok = new X509NameTokenizer(token, '+');
                    this.addEntry(lookUp, pTok.nextToken(), FALSE);

                    while (pTok.hasMoreTokens()) {
                        this.addEntry(lookUp, pTok.nextToken(), TRUE);
                    }
                } else {
                    this.addEntry(lookUp, token, FALSE);
                }
            }

            if (reverse) {
                Vector o = new Vector();
                Vector v = new Vector();
                Vector a = new Vector();
                int count = 1;

                for (int i = 0; i < this.ordering.size(); ++i) {
                    if ((Boolean) this.added.elementAt(i)) {
                        o.insertElementAt(this.ordering.elementAt(i), count);
                        v.insertElementAt(this.values.elementAt(i), count);
                        a.insertElementAt(this.added.elementAt(i), count);
                        ++count;
                    } else {
                        o.insertElementAt(this.ordering.elementAt(i), 0);
                        v.insertElementAt(this.values.elementAt(i), 0);
                        a.insertElementAt(this.added.elementAt(i), 0);
                        count = 1;
                    }
                }

                this.ordering = o;
                this.values = v;
                this.added = a;
            }

            return;
        }
    }

    private void addEntry(Hashtable lookUp, String token, Boolean isAdded) {
        X509NameTokenizer vTok = new X509NameTokenizer(token, '=');
        String name = vTok.nextToken();
        if (!vTok.hasMoreTokens()) {
            throw new IllegalArgumentException("badly formatted directory string");
        } else {
            String value = vTok.nextToken();
            ASN1ObjectIdentifier oid = this.decodeOID(name, lookUp);
            this.ordering.addElement(oid);
            this.values.addElement(this.unescape(value));
            this.added.addElement(isAdded);
        }
    }

    public Vector getOIDs() {
        Vector v = new Vector();

        for (int i = 0; i != this.ordering.size(); ++i) {
            v.addElement(this.ordering.elementAt(i));
        }

        return v;
    }

    public Vector getValues() {
        Vector v = new Vector();

        for (int i = 0; i != this.values.size(); ++i) {
            v.addElement(this.values.elementAt(i));
        }

        return v;
    }

    public Vector getValues(ASN1ObjectIdentifier oid) {
        Vector v = new Vector();

        for (int i = 0; i != this.values.size(); ++i) {
            if (this.ordering.elementAt(i).equals(oid)) {
                String val = (String) this.values.elementAt(i);
                if (val.length() > 2 && val.charAt(0) == '\\' && val.charAt(1) == '#') {
                    v.addElement(val.substring(1));
                } else {
                    v.addElement(val);
                }
            }
        }

        return v;
    }

    public ASN1Primitive toASN1Primitive() {
        if (this.seq == null) {
            ASN1EncodableVector vec = new ASN1EncodableVector();
            ASN1EncodableVector sVec = new ASN1EncodableVector();
            ASN1ObjectIdentifier lstOid = null;

            for (int i = 0; i != this.ordering.size(); ++i) {
                ASN1EncodableVector v = new ASN1EncodableVector();
                ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
                v.add(oid);
                String str = (String) this.values.elementAt(i);
                v.add(this.converter.getConvertedValue(oid, str));
                if (lstOid != null && !(Boolean) this.added.elementAt(i)) {
                    vec.add(new DERSet(sVec));
                    sVec = new ASN1EncodableVector();
                    sVec.add(new DERSequence(v));
                } else {
                    sVec.add(new DERSequence(v));
                }

                lstOid = oid;
            }

            vec.add(new DERSet(sVec));
            this.seq = new DERSequence(vec);
        }

        return this.seq;
    }

    public boolean equals(Object obj, boolean inOrder) {
        if (!inOrder) {
            return this.equals(obj);
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof X509Name) && !(obj instanceof ASN1Sequence)) {
            return false;
        } else {
            ASN1Primitive derO = ((ASN1Encodable) obj).toASN1Primitive();
            if (this.toASN1Primitive().equals(derO)) {
                return true;
            } else {
                X509Name other;
                try {
                    other = getInstance(obj);
                } catch (IllegalArgumentException var11) {
                    return false;
                }

                int orderingSize = this.ordering.size();
                if (orderingSize != other.ordering.size()) {
                    return false;
                } else {
                    for (int i = 0; i < orderingSize; ++i) {
                        ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
                        ASN1ObjectIdentifier oOid = (ASN1ObjectIdentifier) other.ordering.elementAt(i);
                        if (!oid.equals(oOid)) {
                            return false;
                        }

                        String value = (String) this.values.elementAt(i);
                        String oValue = (String) other.values.elementAt(i);
                        if (!this.equivalentStrings(value, oValue)) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }
    }

    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        } else {
            this.isHashCodeCalculated = true;

            for (int i = 0; i != this.ordering.size(); ++i) {
                String value = (String) this.values.elementAt(i);
                value = this.canonicalize(value);
                value = this.stripInternalSpaces(value);
                this.hashCodeValue ^= this.ordering.elementAt(i).hashCode();
                this.hashCodeValue ^= value.hashCode();
            }

            return this.hashCodeValue;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof ASN1Encodable)) {
            return false;
        } else {
            ASN1Primitive derO = ((ASN1Encodable) obj).toASN1Primitive();
            if (this.toASN1Primitive().equals(derO)) {
                return true;
            } else {
                X509Name other;
                try {
                    other = getInstance(obj);
                } catch (IllegalArgumentException var16) {
                    return false;
                }

                int orderingSize = this.ordering.size();
                if (orderingSize != other.ordering.size()) {
                    return false;
                } else {
                    boolean[] indexes = new boolean[orderingSize];
                    int start;
                    int end;
                    byte delta;
                    if (this.ordering.elementAt(0).equals(other.ordering.elementAt(0))) {
                        start = 0;
                        end = orderingSize;
                        delta = 1;
                    } else {
                        start = orderingSize - 1;
                        end = -1;
                        delta = -1;
                    }

                    for (int i = start; i != end; i += delta) {
                        boolean found = false;
                        ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) this.ordering.elementAt(i);
                        String value = (String) this.values.elementAt(i);

                        for (int j = 0; j < orderingSize; ++j) {
                            if (!indexes[j]) {
                                ASN1ObjectIdentifier oOid = (ASN1ObjectIdentifier) other.ordering.elementAt(j);
                                if (oid.equals(oOid)) {
                                    String oValue = (String) other.values.elementAt(j);
                                    if (this.equivalentStrings(value, oValue)) {
                                        indexes[j] = true;
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (!found) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }
    }

    private boolean equivalentStrings(String s1, String s2) {
        String value = this.canonicalize(s1);
        String oValue = this.canonicalize(s2);
        if (!value.equals(oValue)) {
            value = this.stripInternalSpaces(value);
            oValue = this.stripInternalSpaces(oValue);
            if (!value.equals(oValue)) {
                return false;
            }
        }

        return true;
    }

    private String canonicalize(String s) {
        String value = Strings.toLowerCase(s.trim());
        if (value.length() > 0 && value.charAt(0) == '#') {
            ASN1Primitive obj = this.decodeObject(value);
            if (obj instanceof ASN1String) {
                value = Strings.toLowerCase(((ASN1String) obj).getString().trim());
            }
        }

        return value;
    }

    private ASN1Primitive decodeObject(String oValue) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(oValue.substring(1)));
        } catch (IOException var3) {
            IOException e = var3;
            throw new IllegalStateException("unknown encoding in name: " + e);
        }
    }

    private String stripInternalSpaces(String str) {
        StringBuffer res = new StringBuffer();
        if (str.length() != 0) {
            char c1 = str.charAt(0);
            res.append(c1);

            for (int k = 1; k < str.length(); ++k) {
                char c2 = str.charAt(k);
                if (c1 != ' ' || c2 != ' ') {
                    res.append(c2);
                }

                c1 = c2;
            }
        }

        return res.toString();
    }

    private void appendValue(StringBuffer buf, Hashtable oidSymbols, ASN1ObjectIdentifier oid, String value) {
        String sym = (String) oidSymbols.get(oid);
        if (sym != null) {
            buf.append(sym);
        } else {
            buf.append(oid.getId());
        }

        buf.append('=');
        int start = buf.length();
        buf.append(value);
        int end = buf.length();
        if (value.length() >= 2 && value.charAt(0) == '\\' && value.charAt(1) == '#') {
            start += 2;
        }

        while (start < end && buf.charAt(start) == ' ') {
            buf.insert(start, "\\");
            start += 2;
            ++end;
        }

        while (true) {
            --end;
            if (end <= start || buf.charAt(end) != ' ') {
                while (start <= end) {
                    switch (buf.charAt(start)) {
                        case '"':
                        case '+':
                        case ',':
                        case ';':
                        case '<':
                        case '=':
                        case '>':
                        case '\\':
                            buf.insert(start, "\\");
                            start += 2;
                            ++end;
                            break;
                        default:
                            ++start;
                    }
                }

                return;
            }

            buf.insert(end, '\\');
        }
    }

    public String toString(boolean reverse, Hashtable oidSymbols) {
        StringBuffer buf = new StringBuffer();
        Vector components = new Vector();
        boolean first = true;
        StringBuffer ava = null;

        int i;
        for (i = 0; i < this.ordering.size(); ++i) {
            if ((Boolean) this.added.elementAt(i)) {
                ava.append('+');
                this.appendValue(ava, oidSymbols, (ASN1ObjectIdentifier) this.ordering.elementAt(i), (String) this.values.elementAt(i));
            } else {
                ava = new StringBuffer();
                this.appendValue(ava, oidSymbols, (ASN1ObjectIdentifier) this.ordering.elementAt(i), (String) this.values.elementAt(i));
                components.addElement(ava);
            }
        }

        if (reverse) {
            for (i = components.size() - 1; i >= 0; --i) {
                if (first) {
                    first = false;
                } else {
                    buf.append(',');
                }

                buf.append(components.elementAt(i).toString());
            }
        } else {
            for (i = 0; i < components.size(); ++i) {
                if (first) {
                    first = false;
                } else {
                    buf.append(',');
                }

                buf.append(components.elementAt(i).toString());
            }
        }

        return buf.toString();
    }

    private String bytesToString(byte[] data) {
        char[] cs = new char[data.length];

        for (int i = 0; i != cs.length; ++i) {
            cs[i] = (char) (data[i] & 255);
        }

        return new String(cs);
    }

    public String toString() {
        return this.toString(DefaultReverse, DefaultSymbols);
    }

    static {
        SERIALNUMBER = SN;
        L = new ASN1ObjectIdentifier("2.5.4.7");
        ST = new ASN1ObjectIdentifier("2.5.4.8");
        SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
        GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
        INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
        GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
        UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
        BUSINESS_CATEGORY = new ASN1ObjectIdentifier("2.5.4.15");
        POSTAL_CODE = new ASN1ObjectIdentifier("2.5.4.17");
        DN_QUALIFIER = new ASN1ObjectIdentifier("2.5.4.46");
        PSEUDONYM = new ASN1ObjectIdentifier("2.5.4.65");
        DATE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.1");
        PLACE_OF_BIRTH = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.2");
        GENDER = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.3");
        COUNTRY_OF_CITIZENSHIP = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.4");
        COUNTRY_OF_RESIDENCE = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.9.5");
        NAME_AT_BIRTH = new ASN1ObjectIdentifier("1.3.36.8.3.14");
        POSTAL_ADDRESS = new ASN1ObjectIdentifier("2.5.4.16");
        DMD_NAME = new ASN1ObjectIdentifier("2.5.4.54");
        TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
        NAME = X509ObjectIdentifiers.id_at_name;
        EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
        UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
        UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
        E = EmailAddress;
        DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        DefaultReverse = false;
        DefaultSymbols = new Hashtable();
        RFC2253Symbols = new Hashtable();
        RFC1779Symbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        OIDLookUp = DefaultSymbols;
        SymbolLookUp = DefaultLookUp;
        TRUE = Boolean.TRUE;
        FALSE = Boolean.FALSE;
        DefaultSymbols.put(C, "C");
        DefaultSymbols.put(O, "O");
        DefaultSymbols.put(T, "T");
        DefaultSymbols.put(OU, "OU");
        DefaultSymbols.put(CN, "CN");
        DefaultSymbols.put(L, "L");
        DefaultSymbols.put(ST, "ST");
        DefaultSymbols.put(SN, "SERIALNUMBER");
        DefaultSymbols.put(EmailAddress, "E");
        DefaultSymbols.put(DC, "DC");
        DefaultSymbols.put(UID, "UID");
        DefaultSymbols.put(STREET, "STREET");
        DefaultSymbols.put(SURNAME, "SURNAME");
        DefaultSymbols.put(GIVENNAME, "GIVENNAME");
        DefaultSymbols.put(INITIALS, "INITIALS");
        DefaultSymbols.put(GENERATION, "GENERATION");
        DefaultSymbols.put(UnstructuredAddress, "unstructuredAddress");
        DefaultSymbols.put(UnstructuredName, "unstructuredName");
        DefaultSymbols.put(UNIQUE_IDENTIFIER, "UniqueIdentifier");
        DefaultSymbols.put(DN_QUALIFIER, "DN");
        DefaultSymbols.put(PSEUDONYM, "Pseudonym");
        DefaultSymbols.put(POSTAL_ADDRESS, "PostalAddress");
        DefaultSymbols.put(NAME_AT_BIRTH, "NameAtBirth");
        DefaultSymbols.put(COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
        DefaultSymbols.put(COUNTRY_OF_RESIDENCE, "CountryOfResidence");
        DefaultSymbols.put(GENDER, "Gender");
        DefaultSymbols.put(PLACE_OF_BIRTH, "PlaceOfBirth");
        DefaultSymbols.put(DATE_OF_BIRTH, "DateOfBirth");
        DefaultSymbols.put(POSTAL_CODE, "PostalCode");
        DefaultSymbols.put(BUSINESS_CATEGORY, "BusinessCategory");
        DefaultSymbols.put(TELEPHONE_NUMBER, "TelephoneNumber");
        DefaultSymbols.put(NAME, "Name");
        RFC2253Symbols.put(C, "C");
        RFC2253Symbols.put(O, "O");
        RFC2253Symbols.put(OU, "OU");
        RFC2253Symbols.put(CN, "CN");
        RFC2253Symbols.put(L, "L");
        RFC2253Symbols.put(ST, "ST");
        RFC2253Symbols.put(STREET, "STREET");
        RFC2253Symbols.put(DC, "DC");
        RFC2253Symbols.put(UID, "UID");
        RFC1779Symbols.put(C, "C");
        RFC1779Symbols.put(O, "O");
        RFC1779Symbols.put(OU, "OU");
        RFC1779Symbols.put(CN, "CN");
        RFC1779Symbols.put(L, "L");
        RFC1779Symbols.put(ST, "ST");
        RFC1779Symbols.put(STREET, "STREET");
        DefaultLookUp.put("c", C);
        DefaultLookUp.put("o", O);
        DefaultLookUp.put("t", T);
        DefaultLookUp.put("ou", OU);
        DefaultLookUp.put("cn", CN);
        DefaultLookUp.put("l", L);
        DefaultLookUp.put("st", ST);
        DefaultLookUp.put("sn", SN);
        DefaultLookUp.put("serialnumber", SN);
        DefaultLookUp.put("street", STREET);
        DefaultLookUp.put("emailaddress", E);
        DefaultLookUp.put("dc", DC);
        DefaultLookUp.put("e", E);
        DefaultLookUp.put("uid", UID);
        DefaultLookUp.put("surname", SURNAME);
        DefaultLookUp.put("givenname", GIVENNAME);
        DefaultLookUp.put("initials", INITIALS);
        DefaultLookUp.put("generation", GENERATION);
        DefaultLookUp.put("unstructuredaddress", UnstructuredAddress);
        DefaultLookUp.put("unstructuredname", UnstructuredName);
        DefaultLookUp.put("uniqueidentifier", UNIQUE_IDENTIFIER);
        DefaultLookUp.put("dn", DN_QUALIFIER);
        DefaultLookUp.put("pseudonym", PSEUDONYM);
        DefaultLookUp.put("postaladdress", POSTAL_ADDRESS);
        DefaultLookUp.put("nameofbirth", NAME_AT_BIRTH);
        DefaultLookUp.put("countryofcitizenship", COUNTRY_OF_CITIZENSHIP);
        DefaultLookUp.put("countryofresidence", COUNTRY_OF_RESIDENCE);
        DefaultLookUp.put("gender", GENDER);
        DefaultLookUp.put("placeofbirth", PLACE_OF_BIRTH);
        DefaultLookUp.put("dateofbirth", DATE_OF_BIRTH);
        DefaultLookUp.put("postalcode", POSTAL_CODE);
        DefaultLookUp.put("businesscategory", BUSINESS_CATEGORY);
        DefaultLookUp.put("telephonenumber", TELEPHONE_NUMBER);
        DefaultLookUp.put("name", NAME);
    }
}
