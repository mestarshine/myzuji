package com.myzuji.sadk.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class CompatibleConfig {
    public static final int SM2OutputFormatEncryptedBytes;
    public static final int SM2OutputFormatSignedBytes;
    public static final int SM2DecryptedFormatEncryptedBytes;
    public static final int SM2VerifiedFormatSignedBytes;
    public static final boolean SM2VerifiedWithoutZCompatible;
    public static final int P10RequestFormatSignedBytes;
    public static final boolean P10RequestVerifiedSignedFlag;
    public static final int FILEANDBUFFER_BIG_FILE_BUFFER;
    public static final int FILEANDBUFFER_SOURCE_FILE_MAXSIZE;
    public static final int FILEANDBUFFER_SIGNED_FILE_MAXSIZE;
    public static final int FILEANDBUFFER_ENVELOPE_FILE_MAXSIZE;
    public static final int FILEANDBUFFER_BIGGEST_FILE_MAXSIZE;

    public CompatibleConfig() {
    }

    public static void main(String[] args) {
        System.err.println(SM2OutputFormatEncryptedBytes);
        System.err.println(SM2OutputFormatSignedBytes);
        System.err.println(SM2DecryptedFormatEncryptedBytes);
        System.err.println(SM2VerifiedFormatSignedBytes);
        System.err.println(P10RequestVerifiedSignedFlag);
        System.err.println(FILEANDBUFFER_BIG_FILE_BUFFER);
        System.err.println(FILEANDBUFFER_SOURCE_FILE_MAXSIZE);
        System.err.println(FILEANDBUFFER_SIGNED_FILE_MAXSIZE);
        System.err.println(FILEANDBUFFER_ENVELOPE_FILE_MAXSIZE);
        System.err.println(FILEANDBUFFER_BIGGEST_FILE_MAXSIZE);
    }

    private static final Properties loadFromFile() {
        File file = new File("config/compatible.cfg");
        Properties properties = new Properties();
        InputStream in = null;

        try {
            if (file.isFile() && file.exists()) {
                in = new FileInputStream(file);
            } else {
                in = CompatibleConfig.class.getResourceAsStream("cfca.sadk.system.CompatibleConfig");
            }
            if (in == null) {
                in = CompatibleConfig.class.getResourceAsStream("compatible.cfg");
            }

        } catch (Exception var15) {
            Exception e = var15;
            e.printStackTrace();
            in = null;
        }

        if (in != null) {
            try {
                properties.load((InputStream) in);
            } catch (Exception var13) {
            } finally {
                try {
                    ((InputStream) in).close();
                } catch (Exception var12) {
                    Exception e = var12;
                    e.printStackTrace();
                }

            }
        }

        return properties;
    }

    private static final int integerFrom(Properties properties, String key, int defValue) {
        int value = defValue;

        try {
            value = Integer.decode(properties.getProperty(key, Integer.toString(defValue)));
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

        return value;
    }

    private static final boolean booleanFrom(Properties properties, String key, String defValue) {
        return "true".equalsIgnoreCase(properties.getProperty(key, defValue));
    }

    static {
        Properties properties = loadFromFile();
        System.out.println(properties);
        SM2OutputFormatEncryptedBytes = integerFrom(properties, "SM2OutputFormatEncryptedBytes", 1);
        SM2OutputFormatSignedBytes = integerFrom(properties, "SM2OutputFormatSignedBytes", 1);
        SM2DecryptedFormatEncryptedBytes = integerFrom(properties, "SM2DecryptedFormatEncryptedBytes", 21);
        SM2VerifiedWithoutZCompatible = booleanFrom(properties, "SM2VerifiedWithoutZCompatible", "false");
        SM2VerifiedFormatSignedBytes = integerFrom(properties, "SM2VerifiedFormatSignedBytes", 17);
        P10RequestFormatSignedBytes = integerFrom(properties, "P10RequestFormatSignedBytes", 1);
        P10RequestVerifiedSignedFlag = booleanFrom(properties, "P10RequestVerifiedSignedFlag", "true");
        FILEANDBUFFER_BIG_FILE_BUFFER = 1048576 * integerFrom(properties, "FILEANDBUFFER.BIG_FILE_BUFFER", 5);
        FILEANDBUFFER_SOURCE_FILE_MAXSIZE = 1048576 * integerFrom(properties, "FILEANDBUFFER.SOURCE_FILE_MAXSIZE", 50);
        FILEANDBUFFER_SIGNED_FILE_MAXSIZE = 1048576 * integerFrom(properties, "FILEANDBUFFER.SIGNED_FILE_MAXSIZE", 51);
        FILEANDBUFFER_ENVELOPE_FILE_MAXSIZE = 1048576 * integerFrom(properties, "FILEANDBUFFER.ENVELOPE_FILE_MAXSIZE", 51);
        FILEANDBUFFER_BIGGEST_FILE_MAXSIZE = 1048576 * integerFrom(properties, "FILEANDBUFFER.BIGGEST_FILE_MAXSIZE", 55);
    }
}

