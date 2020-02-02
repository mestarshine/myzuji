package com.myzuji.study.java.genericity;

/**
 * 有界泛型 验证
 *
 * @author shine
 * @date 2020/02/02
 */
public class BoundsDemo {
    public static void main(String[] args) {
        Integer inums[] = {1, 2, 3, 4, 5};
        Stats<Integer> iob = new Stats<>(inums);
        double v = iob.average();
        System.out.println("iob average is " + v);

        Double dnums[] = {1.1, 2.2, 3.3, 4.4, 5.5};
        Stats<Double> dob = new Stats<>(dnums);
        double w = dob.average();
        System.out.println("dob average is " + w);

        if (iob.sameAvg(dob)) {
            System.out.println("averages are the same");
        } else {
            System.out.println("averages differ");
        }
    }
}
