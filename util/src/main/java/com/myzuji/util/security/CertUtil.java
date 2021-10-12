package com.myzuji.util.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertUtil {

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCertId(String filePath) {
        X509Certificate encryptCertTemp;
        CertificateFactory cf;
        FileInputStream in = null;
        try {
            cf = CertificateFactory.getInstance("X.509", "BC");
            in = new FileInputStream(filePath);
            encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
            return encryptCertTemp.getSerialNumber().toString();
        } catch (CertificateException e) {
            System.out.println(("InitCert Error" + e));
        } catch (FileNotFoundException e) {
            System.out.println(("InitCert Error File Not Found" + e));
        } catch (NoSuchProviderException e) {
            System.out.println(("LoadVerifyCert Error No BC Provider" + e));
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println((e));
                }
            }
        }
        return "";
    }
}
