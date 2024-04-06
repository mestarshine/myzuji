package com.myzuji.sadk.util.il8n;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

public class I18n {
    protected static Properties properties = new Properties();
    protected static InputStream is = null;

    public I18n() {
    }

    public static String getI18nMessage(String key) {
        String I18nMessage = properties.getProperty(key, key);
        byte[] data = null;

        try {
            data = I18nMessage.getBytes("ISO-8859-1");
            I18nMessage = new String(data, "UTF8");
        } catch (UnsupportedEncodingException var4) {
            UnsupportedEncodingException e = var4;
            e.printStackTrace();
        }

        return I18nMessage;
    }

    public static void setI18nFile(String i18nFilePath) {
    }

    static {
        Locale locale = Locale.getDefault();
        String locLanguage = locale.getLanguage();
        String locCountry = locale.getCountry();
        if ("zh".equals(locLanguage) && "CN".equals(locCountry)) {
            is = I18n.class.getClassLoader().getResourceAsStream("sadkexception_zh_CN.properties");
        }

        try {
            if (null != is) {
                properties.load(is);
            }
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

    }
}
