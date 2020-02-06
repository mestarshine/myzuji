package com.myzuji.study.java.lang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/06
 */
public class ListStudy {

    public static void main(String[] args) {
        List<String> a = new ArrayList(5);
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        a.add("5");
        Iterator iterator1 = a.listIterator();
        Iterator iterator2 = a.listIterator(2);


        while (iterator1.hasNext()) {
            System.out.println("iterator1:"+iterator1.next());
        }
        while (iterator2.hasNext()) {
            System.out.println("iterator2:"+iterator2.next());
        }
    }
}
