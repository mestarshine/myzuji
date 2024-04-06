package com.myzuji.sadk.org.bouncycastle.asn1.x500.style;

public class X500NameTokenizer {
    private String value;
    private int index;
    private char separator;
    private StringBuffer buf;

    public X500NameTokenizer(String oid) {
        this(oid, ',');
    }

    public X500NameTokenizer(String oid, char separator) {
        this.buf = new StringBuffer();
        this.value = oid;
        this.index = -1;
        this.separator = separator;
    }

    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }

    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        } else {
            int end = this.index + 1;
            boolean quoted = false;
            boolean escaped = false;
            this.buf.setLength(0);

            for (; end != this.value.length(); ++end) {
                char c = this.value.charAt(end);
                if (c == '"') {
                    if (!escaped) {
                        quoted = !quoted;
                    }

                    this.buf.append(c);
                    escaped = false;
                } else if (!escaped && !quoted) {
                    if (c == '\\') {
                        this.buf.append(c);
                        escaped = true;
                    } else {
                        if (c == this.separator) {
                            break;
                        }

                        this.buf.append(c);
                    }
                } else {
                    this.buf.append(c);
                    escaped = false;
                }
            }

            this.index = end;
            return this.buf.toString();
        }
    }
}
