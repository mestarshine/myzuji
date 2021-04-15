package com.myzuji.backend.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 *
 * @author shine
 * @date 2020/02/02
 */
public class MessageQueue {

    public static final int QUEUE_MAX_SIZE = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueue.class);
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
        LOGGER.debug("消息{}入队成功", message);
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
            LOGGER.debug("消息出队成功{}", result);
        } catch (InterruptedException e) {
            LOGGER.error("消息出队失败：{}", e.toString());
        }
        return result;
    }

}
