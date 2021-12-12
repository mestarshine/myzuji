package com.myzuji.study.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 从控制台输入参数，回车确定输入的内容，stop 退出
 *
 * @author shine
 * @date 2020/02/02
 */
public class BrReadLines {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        System.out.println("回车确定输入内容");
        System.out.println(" 'stop' 停止输入");
        do {
            str = bufferedReader.readLine();
            System.out.println(str);
        } while (!str.equals("stop"));
    }
}
