package com.myzuji.sadk.org.bouncycastle.asn1.x500.style;

import com.myzuji.sadk.algorithm.common.PKCSObjectIdentifiers;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.RDN;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500Name;
import com.myzuji.sadk.org.bouncycastle.asn1.x500.X500NameStyle;
import com.myzuji.sadk.org.bouncycastle.asn1.x509.X509ObjectIdentifiers;

import java.io.IOException;
import java.util.Hashtable;

public class CFCANameStyle implements X500NameStyle {
    public static final X500NameStyle INSTANCE = new CFCANameStyle();
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
    private static final Hashtable DefaultSymbols;
    private static final Hashtable DefaultLookUp;

    protected CFCANameStyle() {
    }

    public ASN1Encodable stringToValue(ASN1ObjectIdentifier oid, String value) {
        if (value.length() != 0 && value.charAt(0) == '#') {
            try {
                return IETFUtils.valueFromHexString(value, 1);
            } catch (IOException var4) {
                throw new RuntimeException("can't recode value for oid " + oid.getId());
            }
        } else {
            if (value.length() != 0 && value.charAt(0) == '\\') {
                value = value.substring(1);
            }

            if (!oid.equals(EmailAddress) && !oid.equals(DC)) {
                if (oid.equals(DATE_OF_BIRTH)) {
                    return new DERGeneralizedTime(value);
                } else {
                    return (ASN1Encodable) (!oid.equals(C) && !oid.equals(SN) && !oid.equals(DN_QUALIFIER) && !oid.equals(TELEPHONE_NUMBER) ? new DERUTF8String(value) : new DERPrintableString(value));
                }
            } else {
                return new DERIA5String(value);
            }
        }
    }

    public ASN1ObjectIdentifier attrNameToOID(String attrName) {
        return IETFUtils.decodeAttrName(attrName, DefaultLookUp);
    }

    public boolean areEqual(X500Name name1, X500Name name2) {
        RDN[] rdns1 = name1.getRDNs();
        RDN[] rdns2 = name2.getRDNs();
        if (rdns1.length != rdns2.length) {
            return false;
        } else {
            boolean reverse = false;
            if (rdns1[0].getFirst() != null && rdns2[0].getFirst() != null) {
                reverse = !rdns1[0].getFirst().getType().equals(rdns2[0].getFirst().getType());
            }

            for (int i = 0; i != rdns1.length; ++i) {
                if (!this.foundMatch(reverse, rdns1[i], rdns2)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean foundMatch(boolean reverse, RDN rdn, RDN[] possRDNs) {
        int i;
        if (reverse) {
            for (i = possRDNs.length - 1; i >= 0; --i) {
                if (possRDNs[i] != null && this.rdnAreEqual(rdn, possRDNs[i])) {
                    possRDNs[i] = null;
                    return true;
                }
            }
        } else {
            for (i = 0; i != possRDNs.length; ++i) {
                if (possRDNs[i] != null && this.rdnAreEqual(rdn, possRDNs[i])) {
                    possRDNs[i] = null;
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean rdnAreEqual(RDN rdn1, RDN rdn2) {
        if (rdn1.isMultiValued()) {
            if (rdn2.isMultiValued()) {
                AttributeTypeAndValue[] atvs1 = rdn1.getTypesAndValues();
                AttributeTypeAndValue[] atvs2 = rdn2.getTypesAndValues();
                if (atvs1.length != atvs2.length) {
                    return false;
                } else {
                    for (int i = 0; i != atvs1.length; ++i) {
                        if (!this.atvAreEqual(atvs1[i], atvs2[i])) {
                            return false;
                        }
                    }

                    return true;
                }
            } else {
                return false;
            }
        } else {
            return !rdn2.isMultiValued() ? this.atvAreEqual(rdn1.getFirst(), rdn2.getFirst()) : false;
        }
    }

    private boolean atvAreEqual(AttributeTypeAndValue atv1, AttributeTypeAndValue atv2) {
        if (atv1 == atv2) {
            return true;
        } else if (atv1 == null) {
            return false;
        } else if (atv2 == null) {
            return false;
        } else {
            ASN1ObjectIdentifier o1 = atv1.getType();
            ASN1ObjectIdentifier o2 = atv2.getType();
            if (!o1.equals(o2)) {
                return false;
            } else {
                String v1 = IETFUtils.canonicalize(IETFUtils.valueToString(atv1.getValue()));
                String v2 = IETFUtils.canonicalize(IETFUtils.valueToString(atv2.getValue()));
                return v1.equals(v2);
            }
        }
    }

    public RDN[] fromString(String dirName) {
        return IETFUtils.rDNsFromString(dirName, this);
    }

    public int calculateHashCode(X500Name name) {
        int hashCodeValue = 0;
        RDN[] rdns = name.getRDNs();

        for (int i = 0; i != rdns.length; ++i) {
            if (rdns[i].isMultiValued()) {
                AttributeTypeAndValue[] atv = rdns[i].getTypesAndValues();

                for (int j = 0; j != atv.length; ++j) {
                    hashCodeValue ^= atv[j].getType().hashCode();
                    hashCodeValue ^= this.calcHashCode(atv[j].getValue());
                }
            } else {
                hashCodeValue ^= rdns[i].getFirst().getType().hashCode();
                hashCodeValue ^= this.calcHashCode(rdns[i].getFirst().getValue());
            }
        }

        return hashCodeValue;
    }

    private int calcHashCode(ASN1Encodable enc) {
        String value = IETFUtils.valueToString(enc);
        value = IETFUtils.canonicalize(value);
        return value.hashCode();
    }

    public String toString(X500Name name) {
        StringBuffer buf = new StringBuffer();
        boolean first = true;
        RDN[] rdns = name.getRDNs();

        for (int i = rdns.length - 1; i >= 0; --i) {
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }

            if (rdns[i].isMultiValued()) {
                AttributeTypeAndValue[] atv = rdns[i].getTypesAndValues();
                boolean firstAtv = true;

                for (int j = 0; j != atv.length; ++j) {
                    if (firstAtv) {
                        firstAtv = false;
                    } else {
                        buf.append('+');
                    }

                    IETFUtils.appendTypeAndValue(buf, atv[j], DefaultSymbols);
                }
            } else {
                IETFUtils.appendTypeAndValue(buf, rdns[i].getFirst(), DefaultSymbols);
            }
        }

        return buf.toString();
    }

    public String oidToDisplayName(ASN1ObjectIdentifier oid) {
        return (String) DefaultSymbols.get(oid);
    }

    public String[] oidToAttrNames(ASN1ObjectIdentifier oid) {
        return IETFUtils.findAttrNamesForOID(oid, DefaultLookUp);
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
        DefaultSymbols = new Hashtable();
        DefaultLookUp = new Hashtable();
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
