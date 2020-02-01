package com.myzuji.study.java.thread.communication;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Consumer implements Runnable {

    Queue queue;

    public Consumer(Queue queue) {
        this.queue = queue;
        new Thread(this, "Consumer").start();
    }

    @Override
    public void run() {
        while (true) {
            queue.get();
        }
    }
}
