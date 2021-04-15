package com.myzuji.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

/**
 * 权限计算工具类
 *
 * @author shine
 * @date 2020/02/22
 */
public class RightUtil {

    /**
     * 添加权限
     *
     * @param rights 权限值
     * @return
     */
    public static BigInteger addRight(Collection<Long> rights) {
        BigInteger sum = BigInteger.ZERO;
        for (Long right : rights) {
            sum = sum.setBit(right.intValue());
        }
        return sum;
    }

    /**
     * 添加某个权限值
     *
     * @param sum   权限
     * @param right 指定编码
     * @return
     */
    public static BigInteger addRight(BigInteger sum, Long right) {
        return sum.setBit(right.intValue());
    }

    /**
     * 是否拥有指定编码的权限
     *
     * @param sum         权限
     * @param targetRight 指定编码
     * @return
     */
    public static boolean isRight(BigInteger sum, Long targetRight) {
        return sum.testBit(targetRight.intValue());
    }

    /**
     * 移除指定编码的权限
     *
     * @param sum         权限
     * @param targetRight 指定编码
     * @return
     */
    public static BigInteger removeRight(BigInteger sum, Long targetRight) {
        return sum.clearBit(targetRight.intValue());
    }

    public static void main(String[] args) {
        Long[] i = new Long[]{1L, 2L, 3L, 4L, 3L};
        BigInteger rights = addRight(Arrays.asList(i));
        System.out.println(rights);
        System.out.println(isRight(rights, 6L));
        System.out.println(isRight(rights, 3L));
        rights = removeRight(rights, 3L);
        System.out.println(rights);
        System.out.println(isRight(rights, 3L));
    }
}
