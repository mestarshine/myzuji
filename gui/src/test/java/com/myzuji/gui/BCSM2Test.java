package com.myzuji.gui;

import com.myzuji.gui.service.BCSM2KeyUtil;
import com.myzuji.gui.service.BCSM2Util;
import org.junit.jupiter.api.Test;

import java.util.Base64;

public class BCSM2Test {

    @Test
    void buildSM2CSR() throws Exception {
        String subject = "";
        String pwd = "123456";
        BCSM2KeyUtil csr = new BCSM2KeyUtil();
        csr.generateDoublePKCS10(subject, pwd);
        // 可以拿此申请文件数据到CA中申请双证
        System.out.println("[SM2申请P10]: " + csr.getP10Data());
        System.out.println("[SM2签名私钥]: " + csr.getPriKeyBase64());
        System.out.println("[SM2签名私钥HEX]: " + csr.getPriKeyHex());
        System.out.println("[SM2签名公钥]: " + csr.getPubKeyBase64());
        // 临时密钥，用户需要安全保存（加密密钥）
        System.out.println("[SM2加密私钥]: " + csr.getTempPriKeyBase64());
        System.out.println("[SM2加密私钥]: " + csr.getTempPriKeyHex());
        System.out.println("[SM2加密公钥]: " + csr.getTempPubKeyBase64());
    }

    @Test
    void csrParseTest() throws Exception {
        String csrSr = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIIB/DCCAaECAQAwgYExLjAsBgNVBAMMJTA1MUBFQ1BTUyBGQVJNU0BOOTEzMTAx\n" +
            "MDc1NzA3NzU2NzZNQDExGTAXBgNVBAsMEE9yZ2FuaXphdGlvbmFsLTIxDjAMBgNV\n" +
            "BAsMBUZBUk1TMRcwFQYDVQQKDA5DRkNBIFNNMiBPQ0EzMTELMAkGA1UEBhMCQ04w\n" +
            "WTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAASEyXOSgpCZeYVCkYagOgRyq4i3cKjP\n" +
            "NBPWaHbc1vLz13GBlz/QB1xH3rtM8qZg8FwSp690D3qb7i5Hx2qvBlTvoIG8MBUG\n" +
            "CSqGSIb3DQEJBzEIEwYxMjM0NTYwgaIGCSqGSIb3DQEJPzGBlASBkTCBjgIBAQSB\n" +
            "iAC0AAAAAQAAAAzsT0bzE1X+KlSY7DvpgtzLiJJOzRNQiPipp8J+de8AAAAAAAAA\n" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEmXhypXJqsTxgDjhbdD+NGdh5ONvmt9\n" +
            "d16kh9yw0RtxAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAwCgYIKoEc\n" +
            "z1UBg3UDSQAwRgIhAMauqKaxXVaQxEo/dcqmk4+Dyr25IDDfx12ECxFIxNSJAiEA\n" +
            "ri4ADnv7wtlqcS8YuhgiIeTPyfYLOVxzB0OUVrnw28c=\n" +
            "-----END CERTIFICATE REQUEST-----";
        BCSM2KeyUtil sm2KeyUtil = new BCSM2KeyUtil();
        System.out.println("--------------------------");
        sm2KeyUtil.csrParse(csrSr);
        System.out.println("--------------------------");
    }

    @Test
    void decoderTest() throws Exception {
        String hexPriKey = "2EF8622A743C0F595D81DF8BD6FDE65966A20C2C20E8B16C993AC7313F852900";
        String encryptData = "MHgCIFZZHrbvPqBldbiRHv732ZAo/XCSC3Aov7DSGUEMMxloAiBMRdUAdv7vEWlTUbVW+B4+W2SbkfeEL2XnWzzX7QhRAwQgedeq4HXR2DMg2b+8PVs3L5A2JSz4EAowmQMr98yO814EELD6AwSGOWlAcytRG8r8+aw=";

        System.out.println(BCSM2Util.decryptHex(Base64.getDecoder().decode(encryptData), hexPriKey));
    }

    @Test
    void base64toHexTest() {
        String priKeyBase64 = "MIGHAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBG0wawIBAQIgLvhiKnQ8D1ldgd+L1v3mWWaiDCwg6LFsmTrHMT+FKQChRANCAASK4gyv9scETtkdJVHdXX7I+kM10N1yPRXp0/gTKG68L4lNtIa19Dldv7E9dt3BxhiYolbVSejOwNB7AOVfduDh";
        System.out.println(BCSM2Util.base64ToHexKey(priKeyBase64));
    }
}
