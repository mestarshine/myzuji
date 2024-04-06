package com.myzuji.sadk.asn1.parser;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.org.bouncycastle.asn1.*;
import com.myzuji.sadk.org.bouncycastle.util.encoders.Base64Kit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public final class ASN1Parser {
    public ASN1Parser() {
    }

    public static byte[] parseDERObj2Bytes(ASN1Encodable obj) throws PKIException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DEROutputStream dos = new DEROutputStream(bos);

        try {
            dos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception var4) {
            Exception ex = var4;
            throw new PKIException(PKIException.DEROBJ_BYTES, PKIException.DEROBJ_BYTES_DES, ex);
        }
    }

    public static ASN1Object parseBytes2DERObj(byte[] data) throws PKIException {
        ASN1InputStream in = null;

        ASN1Primitive var13;
        try {
            in = new ASN1InputStream(new ByteArrayInputStream(data));
            var13 = in.readObject();
        } catch (Exception var11) {
            Exception ex = var11;
            throw new PKIException(PKIException.BYTES_DEROBJ, PKIException.BYTES_DEROBJ_DES, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    IOException e = var10;
                    throw new PKIException("Parsed DERData failure", e);
                }
            }

        }

        return var13;
    }

    public static ASN1Sequence parseOCT2SEQ(ASN1OctetString asn1oct) throws PKIException {
        ASN1InputStream in = null;

        ASN1Sequence var13;
        try {
            in = new ASN1InputStream(new ByteArrayInputStream(asn1oct.getOctets()));
            var13 = (ASN1Sequence) in.readObject();
        } catch (Exception var11) {
            Exception ex = var11;
            throw new PKIException(PKIException.OCTECT_DER_ERR, PKIException.OCTECT_DER_ERR_DES, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    IOException e = var10;
                    throw new PKIException("Parsed DERData failure", e);
                }
            }

        }

        return var13;
    }

    public static boolean isBase64Compatability(byte[] data) {
        return Base64Kit.isBase64Compatability(data);
    }

    public static byte[] deleteCRLF(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte tmp;
        while ((tmp = (byte) bis.read()) != -1) {
            if (tmp != 10 && tmp != 13) {
                bos.write(tmp);
            }
        }

        return bos.toByteArray();
    }

    public static final ASN1Sequence getDERSequenceFrom(byte[] encoding) throws PKIException {
        try {
            if (isDERSequence(encoding)) {
                return ASN1Sequence.getInstance(encoding);
            } else if (isBERSequence(encoding)) {
                return ASN1Sequence.getInstance(encoding);
            } else {
                byte[] data;
                try {
                    data = Base64.getDecoder().decode(encoding);
                } catch (Exception var3) {
                    Exception e = var3;
                    throw new PKIException("encoding required base64 encoding", e);
                }

                return ASN1Sequence.getInstance(data);
            }
        } catch (PKIException var4) {
            PKIException e = var4;
            throw e;
        } catch (Exception var5) {
            Exception e = var5;
            throw new PKIException("encoding required DERSequence encoding", e);
        }
    }

    public static final boolean isBERSequence(byte[] encoding) throws PKIException {
        if (encoding == null) {
            throw new PKIException("encoding should not be null");
        } else if (encoding.length < 4) {
            throw new PKIException("encoding length less than 4");
        } else if (encoding[0] != 48) {
            return false;
        } else {
            int offset = 1;
            int length = encoding[offset++] & 255;
            if (length != 128) {
                return false;
            } else {
                return encoding[encoding.length - 1] == 0 && encoding[encoding.length - 2] == 0;
            }
        }
    }

    public static final boolean isDERSequence(byte[] encoding) throws PKIException {
        if (encoding == null) {
            throw new PKIException("encoding should not be null");
        } else if (encoding.length < 2) {
            throw new PKIException("encoding length less than 4");
        } else if (encoding[0] != 48) {
            return false;
        } else {
            int offset = 1;
            int length = encoding[offset++] & 255;
            if (length == 128) {
                return false;
            } else {
                if (length > 127) {
                    int dLength = length & 127;
                    if (dLength > 4) {
                        return false;
                    }

                    length = 0;
                    int next = 0;

                    for (int i = 0; i < dLength; ++i) {
                        next = encoding[offset++] & 255;
                        length = (length << 8) + next;
                    }

                    if (length < 0) {
                        return false;
                    }
                }

                return encoding.length == offset + length;
            }
        }
    }
}
