package com.myzuji.study.java.interfaces;

import java.util.ArrayList;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class DefaultInterfaceImpl implements DefaultInterfaceA {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Iterable<Integer> l = (Iterable<Integer>) list.iterator();
    }
}
