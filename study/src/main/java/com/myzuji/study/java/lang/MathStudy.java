package com.myzuji.study.java.lang;

/**
 * java.lang.Math
 *
 * @author shine
 * @date 2020/02/01
 */
public class MathStudy {

    public static void main(String[] args) {
        triangle();
        exponent();
        roundoff();
    }

    private static void triangle() {
        System.out.println("30度的正弦值：" + Math.sin(30D));
        System.out.println("30度的余弦值：" + Math.cos(30D));
        System.out.println("30度的正切值：" + Math.tan(30D));
        System.out.println("30度的正切值：" + Math.tan(30D));
    }

    private static void exponent() {
        System.out.println("8的立方根：" + Math.cbrt(8D));
        System.out.println("e的2次方：" + Math.exp(2D));
        System.out.println("e的(3-1)次方：" + Math.expm1(3D));
        System.out.println("3的对数：" + Math.log(3D));
        System.out.println("3以10为底对数：" + Math.log10(3D));
        System.out.println("3的(3+1)为底对数：" + Math.log1p(3D));
        System.out.println("3的3次方：" + Math.pow(3D,3D));
        System.out.println("3乘以2的6次方：" + Math.scalb(3D,6));
        System.out.println("3乘以2的6次方：" + Math.scalb(3D,6));
        System.out.println("4的平方根：" + Math.sqrt(4D));
    }

    private static void roundoff() {
        System.out.println("绝对值：" + Math.abs(-4D));
        System.out.println("大于或等于 para 的最小整数：" + Math.ceil(3.32D));
        System.out.println("小于或等于 para 的最大整数：" + Math.floor(3.32D));
        System.out.println("返回 para1/para2 的结果的最大整数：" + Math.floorDiv(6, 4));
        System.out.println("从 para1 开始返回 para2 的方向的下一个值：" + Math.nextAfter(6D, 4D));
        System.out.println("返回低于 para 的下一个值：" + Math.nextUp(6));
        System.out.println("返回正方向上 para 的下一个值：" + Math.nextDown(6));
    }
}
