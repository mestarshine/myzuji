package com.myzuji.study.java.lambda;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class MyCompareClass {

    private final int val;

    public MyCompareClass(int val) {
        this.val = val;
    }

    static int compareMcc(MyCompareClass a, MyCompareClass b) {
        return a.getVal() - b.getVal();
    }

    public static void main(String[] args) {
        ArrayList<MyCompareClass> mcc = new ArrayList<>();
        mcc.add(new MyCompareClass(1));
        mcc.add(new MyCompareClass(8));
        mcc.add(new MyCompareClass(2));
        mcc.add(new MyCompareClass(4));
        mcc.add(new MyCompareClass(6));
        mcc.add(new MyCompareClass(7));
        MyCompareClass maxValObject = Collections.max(mcc, MyCompareClass::compareMcc);
    }

    public int getVal() {
        return val;
    }
}
