package com.myzuji.study.java.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/06
 */
public class ListStudy {

    public static void main(String[] args) {
        arrayListIterator();
        arrayListToArray();
    }

    private static void arrayListIterator() {
        ArrayList<String> a = new ArrayList<>(5);
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        a.add("5");
        Iterator iterator1 = a.listIterator();
        Iterator iterator2 = a.listIterator(2);

        while (iterator1.hasNext()) {
            System.out.println("iterator1:" + iterator1.next());
        }
        while (iterator2.hasNext()) {
            System.out.println("iterator2:" + iterator2.next());
        }
    }

    private static void arrayListToArray() {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        System.out.println("contents of a:" + a);
        Integer[] ia = new Integer[a.size()];
        ia = a.toArray(ia);
        int sum = 0;
        for (Integer integer : ia) {
            sum += integer;
            System.out.println("sum is :" + sum);
        }
    }


}
