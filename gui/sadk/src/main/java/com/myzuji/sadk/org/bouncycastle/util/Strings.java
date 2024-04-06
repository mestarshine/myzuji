package com.myzuji.sadk.org.bouncycastle.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public final class Strings {
    public Strings() {
    }

    public static String fromUTF8ByteArray(byte[] bytes) {
        int i = 0;
        int length = 0;

        while (i < bytes.length) {
            ++length;
            if ((bytes[i] & 240) == 240) {
                ++length;
                i += 4;
            } else if ((bytes[i] & 224) == 224) {
                i += 3;
            } else if ((bytes[i] & 192) == 192) {
                i += 2;
            } else {
                ++i;
            }
        }

        char[] cs = new char[length];
        i = 0;

        char ch;
        for (length = 0; i < bytes.length; cs[length++] = ch) {
            if ((bytes[i] & 240) == 240) {
                int codePoint = (bytes[i] & 3) << 18 | (bytes[i + 1] & 63) << 12 | (bytes[i + 2] & 63) << 6 | bytes[i + 3] & 63;
                int U = codePoint - 65536;
                char W1 = (char) ('\ud800' | U >> 10);
                char W2 = (char) ('\udc00' | U & 1023);
                cs[length++] = W1;
                ch = W2;
                i += 4;
            } else if ((bytes[i] & 224) == 224) {
                ch = (char) ((bytes[i] & 15) << 12 | (bytes[i + 1] & 63) << 6 | bytes[i + 2] & 63);
                i += 3;
            } else if ((bytes[i] & 208) == 208) {
                ch = (char) ((bytes[i] & 31) << 6 | bytes[i + 1] & 63);
                i += 2;
            } else if ((bytes[i] & 192) == 192) {
                ch = (char) ((bytes[i] & 31) << 6 | bytes[i + 1] & 63);
                i += 2;
            } else {
                ch = (char) (bytes[i] & 255);
                ++i;
            }
        }

        return new String(cs);
    }

    public static byte[] toUTF8ByteArray(String string) {
        return toUTF8ByteArray(string.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] string) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        try {
            toUTF8ByteArray(string, bOut);
        } catch (IOException var3) {
            throw new IllegalStateException("cannot encode string to byte array!");
        }

        return bOut.toByteArray();
    }

    public static void toUTF8ByteArray(char[] string, OutputStream sOut) throws IOException {
        char[] c = string;

        for (int i = 0; i < c.length; ++i) {
            char ch = c[i];
            if (ch < 128) {
                sOut.write(ch);
            } else if (ch < 2048) {
                sOut.write(192 | ch >> 6);
                sOut.write(128 | ch & 63);
            } else if (ch >= '\ud800' && ch <= '\udfff') {
                if (i + 1 >= c.length) {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }

                char W1 = ch;
                ++i;
                ch = c[i];
                char W2 = ch;
                if (W1 > '\udbff') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }

                int codePoint = ((W1 & 1023) << 10 | W2 & 1023) + 65536;
                sOut.write(240 | codePoint >> 18);
                sOut.write(128 | codePoint >> 12 & 63);
                sOut.write(128 | codePoint >> 6 & 63);
                sOut.write(128 | codePoint & 63);
            } else {
                sOut.write(224 | ch >> 12);
                sOut.write(128 | ch >> 6 & 63);
                sOut.write(128 | ch & 63);
            }
        }

    }

    public static String toUpperCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; ++i) {
            char ch = chars[i];
            if ('a' <= ch && 'z' >= ch) {
                changed = true;
                chars[i] = (char) (ch - 97 + 65);
            }
        }

        if (changed) {
            return new String(chars);
        } else {
            return string;
        }
    }

    public static String toLowerCase(String string) {
        boolean changed = false;
        char[] chars = string.toCharArray();

        for (int i = 0; i != chars.length; ++i) {
            char ch = chars[i];
            if ('A' <= ch && 'Z' >= ch) {
                changed = true;
                chars[i] = (char) (ch - 65 + 97);
            }
        }

        if (changed) {
            return new String(chars);
        } else {
            return string;
        }
    }

    public static byte[] toByteArray(char[] chars) {
        byte[] bytes = new byte[chars.length];

        for (int i = 0; i != bytes.length; ++i) {
            bytes[i] = (byte) chars[i];
        }

        return bytes;
    }

    public static byte[] toByteArray(String string) {
        byte[] bytes = new byte[string.length()];

        for (int i = 0; i != bytes.length; ++i) {
            char ch = string.charAt(i);
            bytes[i] = (byte) ch;
        }

        return bytes;
    }

    public static int toByteArray(String s, byte[] buf, int off) {
        int count = s.length();

        for (int i = 0; i < count; ++i) {
            char c = s.charAt(i);
            buf[off + i] = (byte) c;
        }

        return count;
    }

    public static String fromByteArray(byte[] bytes) {
        return new String(asCharArray(bytes));
    }

    public static char[] asCharArray(byte[] bytes) {
        char[] chars = new char[bytes.length];

        for (int i = 0; i != chars.length; ++i) {
            chars[i] = (char) (bytes[i] & 255);
        }

        return chars;
    }

    public static String[] split(String input, char delimiter) {
        Vector v = new Vector();
        boolean moreTokens = true;

        while (moreTokens) {
            int tokenLocation = input.indexOf(delimiter);
            if (tokenLocation > 0) {
                String subString = input.substring(0, tokenLocation);
                v.addElement(subString);
                input = input.substring(tokenLocation + 1);
            } else {
                moreTokens = false;
                v.addElement(input);
            }
        }

        String[] res = new String[v.size()];

        for (int i = 0; i != res.length; ++i) {
            res[i] = (String) v.elementAt(i);
        }

        return res;
    }
}
