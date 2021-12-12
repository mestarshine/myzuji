package com.myzuji.study.java.annotation;

import java.lang.reflect.Method;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Marker {

    public static void main(String[] args) {
        myMethod();
    }

    @MyMarker
    public static void myMethod() {
        Marker marker = new Marker();
        try {
            Method m = marker.getClass().getMethod("myMethod");
            if (m.isAnnotationPresent(MyMarker.class)) {
                System.out.println("MyMarker is present");
            }
        } catch (NoSuchMethodException e) {
            System.out.println("方法没有找到");
        }
    }
}
