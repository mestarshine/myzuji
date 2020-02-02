package com.myzuji.zujibackend.websocket;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("ws-pull-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
                    TextMessage message = new TextMessage(MessageQueue.getInstance().pollMessage());
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                1, 1, TimeUnit.SECONDS);
    }
}
