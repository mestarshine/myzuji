package com.myzuji.study.java.thread.communication;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public class Producer implements Runnable {

    Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
        new Thread(this, "Producer").start();
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            queue.put(i++);
        }
    }
}
