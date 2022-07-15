package com.myzuji.util;

import com.myzuji.util.security.CertUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

public class RSATest {

    @Test
    @Ignore
    public void getCertIdTest() {
        try {
            System.out.println(CertUtil.getCertId("xxx\\xxx\\xx.cer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
