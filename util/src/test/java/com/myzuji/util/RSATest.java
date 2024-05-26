package com.myzuji.util;

import com.myzuji.util.security.CertUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class RSATest {

    @Test
    @Disabled
    public void getCertIdTest() {
        try {
            System.out.println(CertUtil.getCertId("xxx\\xxx\\xx.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
