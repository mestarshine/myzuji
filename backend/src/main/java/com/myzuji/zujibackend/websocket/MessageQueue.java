package com.myzuji.zujibackend.websocket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 *
 * @author shine
 * @date 2020/02/02
 */
public class MessageQueue {

    public static final int QUEUE_MAX_SIZE = 10000;

    private static MessageQueue messageQueue = new MessageQueue();

    private BlockingQueue<String> messageBlockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private MessageQueue() {
    }

    public static MessageQueue getInstance() {
        return messageQueue;
    }

    /**
     * 消息入队
     *
     * @param message
     * @return
     */
    public boolean pushMessage(String message) {
        return this.messageBlockingQueue.add(message);
    }

    /**
     * 消息出队
     *
     * @return
     */
    public String pollMessage() {
        String result = null;
        try {
            result = this.messageBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}
