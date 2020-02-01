package com.myzuji.study.java.lang;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class StudyString {

    public static void main(String[] args) {
        System.out.println("String =====================");
        String a = "join 1";
        System.out.println(String.join("", a, "join2"));
        System.out.println(a.codePointAt(a.length() - 1));
        System.out.println(a.codePointBefore(a.length() - 1));
        System.out.println(a.subSequence(0, 2));
        System.out.println(a.length());
        System.out.println("StringBuffer ==================");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("1234123412341234112123412341234344543454454345345");
        System.out.println(stringBuffer.capacity());
        System.out.println(Integer.BYTES);
        System.out.println(Float.BYTES);
        System.out.println(Double.BYTES);
        System.out.println(Long.BYTES);
        System.out.println(Long.hashCode(Long.decode("12")));
        System.out.println(Long.reverse(10011));
        System.out.println(Long.reverseBytes(1021));
        System.out.println(Long.rotateLeft(1231, 1));
        System.out.println(Long.rotateRight(2462, 1));
        System.out.println(Character.toTitleCase(new Character('a')));
    }
}
