package com.myzuji.study;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class TmpTest {

    @Test
    void randomMoney() {
        int max = 10000;
        int sum = 0;
        for (; sum <= max; ) {
            int rdm = RandomUtils.nextInt(600, 900);
            sum += rdm;
            System.out.println(rdm);
        }
        System.out.println("总和：" + sum);
    }
}
