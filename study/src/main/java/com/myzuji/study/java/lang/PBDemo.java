package com.myzuji.study.java.lang;

import java.io.IOException;

public class PBDemo {

    /**
     * 需在windows环境下运行
     * @param args
     */
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder("notepad.exe");
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("关闭");
    }
}
