package com.myzuji.sadk.org.bouncycastle.asn1;

import com.myzuji.sadk.org.bouncycastle.util.Arrays;
import com.myzuji.sadk.org.bouncycastle.util.Strings;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class ASN1GeneralizedTime extends ASN1Primitive {
    private byte[] time;

    public static ASN1GeneralizedTime getInstance(Object obj) {
        if (obj != null && !(obj instanceof ASN1GeneralizedTime)) {
            if (obj instanceof byte[]) {
                try {
                    return (ASN1GeneralizedTime) fromByteArray((byte[]) ((byte[]) obj));
                } catch (Exception var2) {
                    Exception e = var2;
                    throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
                }
            } else {
                throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
            }
        } else {
            return (ASN1GeneralizedTime) obj;
        }
    }

    public static ASN1GeneralizedTime getInstance(ASN1TaggedObject obj, boolean explicit) {
        ASN1Primitive o = obj.getObject();
        return !explicit && !(o instanceof ASN1GeneralizedTime) ? new ASN1GeneralizedTime(((ASN1OctetString) o).getOctets()) : getInstance(o);
    }

    public ASN1GeneralizedTime(String time) {
        this.time = Strings.toByteArray(time);

        try {
            this.getDate();
        } catch (ParseException var3) {
            ParseException e = var3;
            throw new IllegalArgumentException("invalid date string: " + e.getMessage());
        }
    }

    public ASN1GeneralizedTime(Date time) {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(dateF.format(time));
    }

    public ASN1GeneralizedTime(Date time, Locale locale) {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss'Z'", locale);
        dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(dateF.format(time));
    }

    ASN1GeneralizedTime(byte[] bytes) {
        this.time = bytes;
    }

    public String getTimeString() {
        return Strings.fromByteArray(this.time);
    }

    public String getTime() {
        String stime = Strings.fromByteArray(this.time);
        if (stime.charAt(stime.length() - 1) == 'Z') {
            return stime.substring(0, stime.length() - 1) + "GMT+00:00";
        } else {
            int signPos = stime.length() - 5;
            char sign = stime.charAt(signPos);
            if (sign != '-' && sign != '+') {
                signPos = stime.length() - 3;
                sign = stime.charAt(signPos);
                return sign != '-' && sign != '+' ? stime + this.calculateGMTOffset() : stime.substring(0, signPos) + "GMT" + stime.substring(signPos) + ":00";
            } else {
                return stime.substring(0, signPos) + "GMT" + stime.substring(signPos, signPos + 3) + ":" + stime.substring(signPos + 3);
            }
        }
    }

    private String calculateGMTOffset() {
        String sign = "+";
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset();
        if (offset < 0) {
            sign = "-";
            offset = -offset;
        }

        int hours = offset / 3600000;
        int minutes = (offset - hours * 60 * 60 * 1000) / '\uea60';

        try {
            if (timeZone.useDaylightTime() && timeZone.inDaylightTime(this.getDate())) {
                hours += sign.equals("+") ? 1 : -1;
            }
        } catch (ParseException var7) {
        }

        return "GMT" + sign + this.convert(hours) + ":" + this.convert(minutes);
    }

    private String convert(int time) {
        return time < 10 ? "0" + time : Integer.toString(time);
    }

    public Date getDate() throws ParseException {
        String stime = Strings.fromByteArray(this.time);
        String d = stime;
        SimpleDateFormat dateF;
        if (stime.endsWith("Z")) {
            if (this.hasFractionalSeconds()) {
                dateF = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
            } else {
                dateF = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
            }

            dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        } else if (stime.indexOf(45) <= 0 && stime.indexOf(43) <= 0) {
            if (this.hasFractionalSeconds()) {
                dateF = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
            } else {
                dateF = new SimpleDateFormat("yyyyMMddHHmmss");
            }

            dateF.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
        } else {
            d = this.getTime();
            if (this.hasFractionalSeconds()) {
                dateF = new SimpleDateFormat("yyyyMMddHHmmss.SSSz");
            } else {
                dateF = new SimpleDateFormat("yyyyMMddHHmmssz");
            }

            dateF.setTimeZone(new SimpleTimeZone(0, "Z"));
        }

        if (this.hasFractionalSeconds()) {
            String frac = d.substring(14);

            int index;
            for (index = 1; index < frac.length(); ++index) {
                char ch = frac.charAt(index);
                if ('0' > ch || ch > '9') {
                    break;
                }
            }

            if (index - 1 > 3) {
                frac = frac.substring(0, 4) + frac.substring(index);
                d = d.substring(0, 14) + frac;
            } else if (index - 1 == 1) {
                frac = frac.substring(0, index) + "00" + frac.substring(index);
                d = d.substring(0, 14) + frac;
            } else if (index - 1 == 2) {
                frac = frac.substring(0, index) + "0" + frac.substring(index);
                d = d.substring(0, 14) + frac;
            }
        }

        return dateF.parse(d);
    }

    private boolean hasFractionalSeconds() {
        for (int i = 0; i != this.time.length; ++i) {
            if (this.time[i] == 46 && i == 14) {
                return true;
            }
        }

        return false;
    }

    public boolean isConstructed() {
        return false;
    }

    public int encodedLength() {
        int length = this.time.length;
        return 1 + StreamUtil.calculateBodyLength(length) + length;
    }

    public void encode(ASN1OutputStream out) throws IOException {
        out.writeEncoded(24, this.time);
    }

    public boolean asn1Equals(ASN1Primitive o) {
        return !(o instanceof ASN1GeneralizedTime) ? false : Arrays.areEqual(this.time, ((ASN1GeneralizedTime) o).time);
    }

    public int hashCode() {
        return Arrays.hashCode(this.time);
    }
}
