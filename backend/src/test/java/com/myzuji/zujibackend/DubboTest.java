package com.myzuji.zujibackend;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
public class DubboTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/dubbo-consumer.xml"});
        context.start();
    }
}
