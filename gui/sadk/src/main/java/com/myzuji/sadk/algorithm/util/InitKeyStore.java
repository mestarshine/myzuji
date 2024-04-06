package com.myzuji.sadk.algorithm.util;

import com.myzuji.sadk.algorithm.common.PKIException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class InitKeyStore {
    public InitKeyStore() {
    }

    public static KeyStore initPKCS12KeyStore(byte[] pfxData, String pfxPWD) throws Exception {
        KeyStore ks = null;
        ByteArrayInputStream bis = null;

        KeyStore var5;
        try {
            Exception e;
            try {
                ks = KeyStore.getInstance("PKCS12");
                e = null;
                char[] nPassword;
                if (pfxPWD != null && !pfxPWD.trim().equals("")) {
                    nPassword = pfxPWD.toCharArray();
                } else {
                    nPassword = null;
                }

                bis = new ByteArrayInputStream(pfxData);
                ks.load(bis, nPassword);
                var5 = ks;
            } catch (Exception var9) {
                e = var9;
                throw new PKIException("load PKCS#12 failure", e);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }

        }

        return var5;
    }

    public static KeyStore initJKSKeyStore(String jksFilePath, String jksPWD) throws Exception {
        FileInputStream fin = null;

        KeyStore var5;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            fin = new FileInputStream(jksFilePath);
            char[] password = jksPWD.toCharArray();
            ks.load(fin, password);
            var5 = ks;
        } catch (Exception var9) {
            Exception e = var9;
            throw new PKIException("load JKS failure", e);
        } finally {
            if (fin != null) {
                fin.close();
            }

        }

        return var5;
    }

    public static KeyStore initJKSKeyStore(InputStream is, String jksPWD) throws Exception {
        KeyStore var4;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] password = jksPWD.toCharArray();
            ks.load(is, password);
            var4 = ks;
        } catch (Exception var8) {
            Exception e = var8;
            throw new PKIException("load JKS failure", e);
        } finally {
            if (is != null) {
                is.close();
            }

        }

        return var4;
    }
}
