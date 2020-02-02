package com.myzuji.study.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 从控制台输入参数，q 退出
 *
 * @author shine
 * @date 2020/02/02
 */
public class BrRead {

    public static void main(String[] args) throws IOException {
        char c;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("q退出");
        do {
            c = (char) bufferedReader.read();
            System.out.println(c);
        } while (c != 'q');
    }

}
