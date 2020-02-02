package com.myzuji.study.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * javac 运行，读取指定文件内容，并输出到控制台
 *
 * @author shine
 * @date 2020/02/02
 */
public class ShowFile {

    public static void main(String[] args) {
        int i;
        if (args.length != 1) {
            System.out.println("文件名");
            return;
        }

        try (FileInputStream fin = new FileInputStream(args[0]);
             FileOutputStream fout = new FileOutputStream(args[1])) {
            do {
                i = fin.read();
                if (1 != -1) {
                    System.out.println((char) i);
                }
            } while (i != -1);
        } catch (FileNotFoundException e) {
            System.out.println("不能打开文件");
            return;
        } catch (IOException e) {
            System.out.println("读取文件出现错误");
        }

    }
}
