package com.myzuji.study.java.lambda;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class MethodRefDemo {

    static String stringOp(StringFunc sf, String s) {
        return sf.func(s);
    }

    static <T> int counter(T[] vals, MyFunc<T> f, T v) {
        int count = 0;
        for (int i = 0; i < vals.length; i++) {
            if (f.func(vals[i], v)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String inStr = "lambdas 增强了 java 功能";
        System.out.println("原始字符串：" + inStr);
        //静态方法的方法引用
        System.out.println("反写：" + stringOp(MyStringOps::strReverses, inStr));

        //实例方法的方法引用
        MyStringOps myStringOps = new MyStringOps();
        System.out.println("反写：" + stringOp(myStringOps::strReverse, inStr));

        int count;
        HighTemp[] weekDayHighs = {new HighTemp(89), new HighTemp(82),
            new HighTemp(90), new HighTemp(89), new HighTemp(84),
            new HighTemp(91), new HighTemp(82)};
        count = counter(weekDayHighs, HighTemp::sameTemp, new HighTemp(89));
    }
}
