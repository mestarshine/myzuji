package com.myzuji.sadk.lib.crypto;

import com.myzuji.sadk.algorithm.common.PKIException;
import com.myzuji.sadk.lib.crypto.bcsoft.BCSoftLib;
import com.myzuji.sadk.lib.crypto.jni.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class JCrypto {
    public static final String JSOFT_LIB = "JSOFT_LIB";
    public static final String JNI_LIB = "JNISOFT_LIB";
    public static final String JHARD_LIB = "JHARD_LIB";
    private static JCrypto jCrypto = null;
    private Hashtable htable = new Hashtable();

    private JCrypto() {
    }

    public static synchronized JCrypto getInstance() {
        if (jCrypto == null) {
            jCrypto = new JCrypto();
            return jCrypto;
        } else {
            return jCrypto;
        }
    }

    public Session openSession(String deviceName) throws PKIException {
        Session session = (Session) this.htable.get(deviceName);
        if (session == null) {
            throw new PKIException(PKIException.OPSESSION, PKIException.OPSESSION_DES + " " + deviceName);
        } else {
            return session;
        }
    }

    public boolean initialize(String deviceName, Object param) throws PKIException {
        try {
            if (this.htable.containsKey(deviceName)) {
                return true;
            } else {
                if (deviceName.equals("JSOFT_LIB")) {
                    this.htable.put(deviceName, new BCSoftLib());
                }

                return true;
            }
        } catch (Exception var4) {
            Exception ex = var4;
            throw new PKIException(PKIException.INIT, PKIException.INIT_DES + " " + deviceName, ex);
        }
    }

    public long formateLocalTime(String localTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        date = simpleDateFormat.parse(localTime);
        return date.getTime();
    }

    public boolean finalize(String deviceName, Object param) throws PKIException {
        try {
            if (!this.htable.containsKey(deviceName)) {
                return true;
            } else {
                this.htable.remove(deviceName);
                return true;
            }
        } catch (Exception var4) {
            Exception ex = var4;
            throw new PKIException(PKIException.FINI, PKIException.FINI_DES + " " + deviceName, ex);
        }
    }
}
