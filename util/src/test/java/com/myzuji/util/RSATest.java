package com.myzuji.util;

import com.myzuji.util.security.CertUtil;
import org.junit.Test;

public class RSATest {

    @Test
    public void getCertIdTest() {
        try {
            System.out.println(CertUtil.getCertId("xxx\\xxx\\xx.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
