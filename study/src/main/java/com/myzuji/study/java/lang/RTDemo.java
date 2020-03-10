package com.myzuji.study.java.lang;

public class RTDemo {

    /**
     * 通过 Runtime 的 exec 调起windows中的记事本
     * 需在windows环境下运行
     * @param args
     */
    public static void main(String[] args) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("notepad");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("notepad return" + process.exitValue());
    }
}
