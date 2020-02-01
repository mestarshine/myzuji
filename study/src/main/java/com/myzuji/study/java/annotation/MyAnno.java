package com.myzuji.study.java.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {

    String str() default "为空";

    int val() default 0;
}
