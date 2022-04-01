package com.myzuji.study.java.lang;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class RunTimeStudy {

    public static void main(String[] args) {
        try {
            ProcessBuilder p = new ProcessBuilder("notepad.exe", "testfile");
            p.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
