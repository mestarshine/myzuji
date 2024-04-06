package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class ASN1UTCTime extends ASN1Primitive {
    private byte[] time;

    public static ASN1UTCTime getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1UTCTime)) {
            if (obj instanceof byte[]) {
                try {
                    return (ASN1UTCTime) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1UTCTime) obj;
        }
    }

    public static ASN1UTCTime getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Object o = obj.getObject();
        return !explicit && !(o instanceof ASN1UTCTime) ? new ASN1UTCTime(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    public ASN1UTCTime(String time) {
        this.time = Strings.toByteArray(time);

        try {
            this.getDate();
        } catch (ParseException var3) {
            ParseException e = var3;
            throw new IllegalArgumentException("invalid date string: " + e.getMessage());
        }
    }

    public ASN1UTCTime(Date time) {
        SimpleDateFormat dateF = new SimpleDateFormat("yyMMddHHmmss'Z'");
        dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(dateF.format(time));
    }

    public ASN1UTCTime(Date time, Locale locale) {
        SimpleDateFormat dateF = new SimpleDateFormat("yyMMddHHmmss'Z'", locale);
        dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(dateF.format(time));
    }

    ASN1UTCTime(byte[] time) {
        this.time = time;
    }

    public Date getDate() throws ParseException {
        SimpleDateFormat dateF = new SimpleDateFormat("yyMMddHHmmssz");
        return dateF.parse(this.getTime());
    }

    public Date getAdjustedDate() throws ParseException {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmssz");
        dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        return dateF.parse(this.getAdjustedTime());
    }

    public String getTime() {
        String stime = Strings.fromByteArray(this.time);
        if (stime.indexOf(45) < 0 && stime.indexOf(43) < 0) {
            return stime.length() == 11 ? stime.substring(0, 10) + "00GMT+00:00" : stime.substring(0, 12) + "GMT+00:00";
        } else {
            int index = stime.indexOf(45);
            if (index < 0) {
                index = stime.indexOf(43);
            }

            String d = stime;
            if (index == stime.length() - 3) {
                d = d + "00";
            }

            return index == 10 ? d.substring(0, 10) + "00GMT" + d.substring(10, 13) + ":" + d.substring(13, 15) : d.substring(0, 12) + "GMT" + d.substring(12, 15) + ":" + d.substring(15, 17);
        }
    }

    public String getAdjustedTime() {
        String d = this.getTime();
        return d.charAt(0) < '5' ? "20" + d : "19" + d;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        int length = this.time.length;
        return 1 + StreamUtil.calculateBodyLength(length) + length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.write(23);
        int length = this.time.length;
        out.writeLength(length);

        for (int i = 0; i != length; ++i) {
            out.write(this.time[i]);
        }

    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof ASN1UTCTime) ? false : Arrays.areEqual(this.time, ((ASN1UTCTime) o).time);
    }

    public int hashCode() {
        return Arrays.hashCode(this.time);
    }

    public String toString() {
        return Strings.fromByteArray(this.time);
    }
}
