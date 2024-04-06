package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class ASN1ObjectIdentifier extends ASN1Primitive {
    String identifier;
    private byte[] body;
    private static final long LONG_LIMIT = 72057594037927808L;
    private static ASN1ObjectIdentifier[][] cache = new ASN1ObjectIdentifier[256][];

    public static ASN1ObjectIdentifier getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1ObjectIdentifier)) {
            if (obj instanceof ASN1Encodable && ((ASN1Encodable) obj).toASN1Primitive() instanceof ASN1ObjectIdentifier) {
                return (ASN1ObjectIdentifier) ((ASN1Encodable) obj).toASN1Primitive();
            } else if (obj instanceof byte[]) {
                byte[] enc = (byte[]) ((byte[]) obj);

                try {
                    return (ASN1ObjectIdentifier) fromByteArray(enc);
                } catch (IOException var3) {
                    IOException e = var3;
                    throw new IllegalArgumentException("failed to construct object identifier from byte[]: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1ObjectIdentifier) obj;
        }
    }

    public static ASN1ObjectIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof ASN1ObjectIdentifier) ? fromOctetString(ASN1OctetString.getInstance(obj.getObject()).getOctets()) : getInstance(o);
    }

    ASN1ObjectIdentifier(byte[] bytes) {
        StringBuffer objId = new StringBuffer();
        long value = 0L;
        BigInteger bigValue = null;
        boolean first = true;

        for (int i = 0; i != bytes.length; ++i) {
            int b = bytes[i] & 255;
            if (value <= 72057594037927808L) {
                value += (long) (b & 127);
                if ((b & 128) == 0) {
                    if (first) {
                        if (value < 40L) {
                            objId.append('0');
                        } else if (value < 80L) {
                            objId.append('1');
                            value -= 40L;
                        } else {
                            objId.append('2');
                            value -= 80L;
                        }

                        first = false;
                    }

                    objId.append('.');
                    objId.append(value);
                    value = 0L;
                } else {
                    value <<= 7;
                }
            } else {
                if (bigValue == null) {
                    bigValue = BigInteger.valueOf(value);
                }

                bigValue = bigValue.or(BigInteger.valueOf((long) (b & 127)));
                if ((b & 128) == 0) {
                    if (first) {
                        objId.append('2');
                        bigValue = bigValue.subtract(BigInteger.valueOf(80L));
                        first = false;
                    }

                    objId.append('.');
                    objId.append(bigValue);
                    bigValue = null;
                    value = 0L;
                } else {
                    bigValue = bigValue.shiftLeft(7);
                }
            }
        }

        this.identifier = objId.toString();
        this.body = Arrays.clone(bytes);
    }

    public ASN1ObjectIdentifier(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("'identifier' cannot be null");
        } else if (!isValidIdentifier(identifier)) {
            throw new IllegalArgumentException("string " + identifier + " not an OID");
        } else {
            this.identifier = identifier;
        }
    }

    ASN1ObjectIdentifier(ASN1ObjectIdentifier oid, String branchID) {
        if (!isValidBranchID(branchID, 0)) {
            throw new IllegalArgumentException("string " + branchID + " not a valid OID branch");
        } else {
            this.identifier = oid.getId() + "." + branchID;
        }
    }

    public String getId() {
        return this.identifier;
    }

    public ASN1ObjectIdentifier branch(String branchID) {
        return new ASN1ObjectIdentifier(this, branchID);
    }

    public boolean on(ASN1ObjectIdentifier stem) {
        String id = this.getId();
        String stemId = stem.getId();
        return id.length() > stemId.length() && id.charAt(stemId.length()) == '.' && id.startsWith(stemId);
    }

    private void writeField(ByteArrayOutputStream out, long fieldValue) {
        byte[] result = new byte[9];
        int pos = 8;

        for (result[pos] = (byte) ((int) fieldValue & 127); fieldValue >= 128L; result[pos] = (byte) ((int) fieldValue & 127 | 128)) {
            fieldValue >>= 7;
            --pos;
        }

        out.write(result, pos, 9 - pos);
    }

    private void writeField(ByteArrayOutputStream out, BigInteger fieldValue) {
        int byteCount = (fieldValue.bitLength() + 6) / 7;
        if (byteCount == 0) {
            out.write(0);
        } else {
            BigInteger tmpValue = fieldValue;
            byte[] tmp = new byte[byteCount];

            for (int i = byteCount - 1; i >= 0; --i) {
                tmp[i] = (byte) (tmpValue.intValue() & 127 | 128);
                tmpValue = tmpValue.shiftRight(7);
            }

            tmp[byteCount - 1] = (byte) (tmp[byteCount - 1] & 127);
            out.write(tmp, 0, tmp.length);
        }

    }

    private void doOutput(ByteArrayOutputStream aOut) {
        OIDTokenizer tok = new OIDTokenizer(this.identifier);
        int first = Integer.parseInt(tok.nextToken()) * 40;
        String secondToken = tok.nextToken();
        if (secondToken.length() <= 18) {
            this.writeField(aOut, (long) first + Long.parseLong(secondToken));
        } else {
            this.writeField(aOut, (new BigInteger(secondToken)).add(BigInteger.valueOf((long) first)));
        }

        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            if (token.length() <= 18) {
                this.writeField(aOut, Long.parseLong(token));
            } else {
                this.writeField(aOut, new BigInteger(token));
            }
        }

    }

    protected synchronized byte[] getBody() {
        if (this.body == null) {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            this.doOutput(bOut);
            this.body = bOut.toByteArray();
        }

        return this.body;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() throws IOException {
        int length = this.getBody().length;
        return 1 + StreamUtil.calculateBodyLength(length) + length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        byte[] enc = this.getBody();
        out.write(6);
        out.writeLength(enc.length);
        out.write(enc);
    }

    public int hashCode() {
        return this.identifier.hashCode();
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof ASN1ObjectIdentifier) ? false : this.identifier.equals(((ASN1ObjectIdentifier) o).identifier);
    }

    public String toString() {
        return this.getId();
    }

    private static boolean isValidBranchID(String branchID, int start) {
        boolean periodAllowed = false;
        int pos = branchID.length();

        while (true) {
            while (true) {
                --pos;
                if (pos < start) {
                    return periodAllowed;
                }

                char ch = branchID.charAt(pos);
                if ('0' <= ch && ch <= '9') {
                    periodAllowed = true;
                } else {
                    if (ch != '.') {
                        return false;
                    }

                    if (!periodAllowed) {
                        return false;
                    }

                    periodAllowed = false;
                }
            }
        }
    }

    private static boolean isValidIdentifier(String identifier) {
        if (identifier.length() >= 3 && identifier.charAt(1) == '.') {
            char first = identifier.charAt(0);
            return first >= '0' && first <= '2' ? isValidBranchID(identifier, 2) : false;
        } else {
            return false;
        }
    }

    static ASN1ObjectIdentifier fromOctetString(byte[] enc) {
        if (enc.length < 3) {
            return new ASN1ObjectIdentifier(enc);
        } else {
            int idx1 = enc[enc.length - 2] & 255;
            int idx2 = enc[enc.length - 1] & 127;
            ASN1ObjectIdentifier possibleMatch;
            synchronized (cache) {
                ASN1ObjectIdentifier[] first = cache[idx1];
                if (first == null) {
                    first = cache[idx1] = new ASN1ObjectIdentifier[128];
                }

                possibleMatch = first[idx2];
                if (possibleMatch == null) {
                    return first[idx2] = new ASN1ObjectIdentifier(enc);
                }

                if (Arrays.areEqual(enc, possibleMatch.getBody())) {
                    return possibleMatch;
                }

                idx1 = idx1 + 1 & 255;
                first = cache[idx1];
                if (first == null) {
                    first = cache[idx1] = new ASN1ObjectIdentifier[128];
                }

                possibleMatch = first[idx2];
                if (possibleMatch == null) {
                    return first[idx2] = new ASN1ObjectIdentifier(enc);
                }

                if (Arrays.areEqual(enc, possibleMatch.getBody())) {
                    return possibleMatch;
                }

                idx2 = idx2 + 1 & 127;
                possibleMatch = first[idx2];
                if (possibleMatch == null) {
                    return first[idx2] = new ASN1ObjectIdentifier(enc);
                }
            }

            return Arrays.areEqual(enc, possibleMatch.getBody()) ? possibleMatch : new ASN1ObjectIdentifier(enc);
        }
    }
}
