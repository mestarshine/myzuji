package com.myzuji.study.java.thread.communication;

/**
 * 一个简单的生产者消费者示例，分析线程之间的通信
 *
 * @author shine
 * @date 2020/02/01
 */
public class Test {

    public static void main(String[] args) {
        Queue queue = new Queue();
        new Producer(queue);
        new Consumer(queue);
        System.out.println("Press Ctr+c top stop.");
    }
}
