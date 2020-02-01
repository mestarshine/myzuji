package com.myzuji.study.java.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Meta {

    public static void main(String[] args) {
//        myMethod();
//        myMethod("test",2);
        myMethod3("test", 2);
    }

    @MyAnno(str = "Annotation Example1", val = 1)
    public static void myMethod() {
        Class<?> c = Meta.class;
        try {
            Method m = c.getMethod("myMethod");
            MyAnno anno = m.getDeclaredAnnotation(MyAnno.class);
            System.out.println(anno.str() + " " + anno.val());
        } catch (NoSuchMethodException e) {
            System.out.println("方法没有找到");
        }
    }

    @MyAnno(str = "Annotation Example2", val = 2)
    public static void myMethod(String str, int i) {
        Class<?> c = Meta.class;
        try {
            Method m = c.getMethod("myMethod", String.class, int.class);
            MyAnno anno = m.getDeclaredAnnotation(MyAnno.class);
            System.out.println(anno.str() + " " + anno.val());
        } catch (NoSuchMethodException e) {
            System.out.println("方法没有找到");
        }
    }

    @What(description = "an annotation test method")
    @MyAnno(str = "Annotation Example3", val = 3)
    public static void myMethod3(String str, int i) {
        try {
            Class<?> c = Meta.class;
            Annotation[] annotations = c.getAnnotations();
            System.out.println("all annotations for Meta class");
            for (Annotation annotation : annotations) {
                System.out.println(annotation);
            }
            System.out.println();
            Method m = c.getMethod("myMethod3", String.class, int.class);
            annotations = m.getAnnotations();
            System.out.println("all annotations for myMethod3 method");
            for (Annotation annotation : annotations) {
                System.out.println(annotation);
            }
            System.out.println();
        } catch (NoSuchMethodException e) {
            System.out.println("方法没有找到");
        }
    }

}
