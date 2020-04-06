package com.myzuji.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static com.myzuji.util.security.SM2Tool.*;


/**
 * 国密SM2测试
 *
 * @author shine
 * @date 2020/04/05
 */
public class SM2ToolTest {

    @Test
    public void genSMKeyPairTest() {
        Map<String, String> key = genSMKeyPair();
        Assert.assertNotNull("密钥生成失败", key);
        System.out.println("pri:" + key.get("privateKey"));
        System.out.println("pub:" + key.get("publicKey"));
    }

    @Test
    public void singTest() {
        String pri = "MI`GTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgIaL7KnN9lFMPreIjCoKhjRe2cfduaMHHN9YitKy8SFGgCgYIKoEcz1UBgi2hRANCAATwZCTbWFI1eORRlWzT4YLOhPQ4kyAUBStaf+2+Gi/LU+FKtfWnOlMpc/+PsQbzlD7EjSCXWEOI6OhPO2kA58xJ";
        String content = "SM2你好";
        String signInfo = sign(pri, content);
        Assert.assertNotNull(signInfo);
        System.out.println(("signInfo:" + signInfo));
    }

    @Test
    public void verifyTest() {
        String pub = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE8GQk21hSNXjkUZVs0+GCzoT0OJMgFAUrWn/tvhovy1PhSrX1pzpTKXP/j7EG85Q+xI0gl1hDiOjoTztpAOfMSQ==";
        String content = "SM2你好";
        String signInfo = "MEUCIQCyqspu8MB8BDsJovywpSgp62SO3x26oU6p14tamjTOUQIgHY/Dnn0eN0Bi1J6CYjUP8WhNB3GTnwzdnihohrrsrkI=";
        boolean verify = verify(pub, content, signInfo);
        System.out.println("verify:" + verify);
    }

}
