package com.myzuji.backend.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private static Map<String, WebSocketSession> users = Collections.emptyMap();

    public static void sendMessageAllUser(String message) {
        for (WebSocketSession webSocketSession : users.values()) {
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    logger.error("向【{}】消息发送失败！", webSocketSession.getId());
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("{} - 成功建立websocket连接!", session.getId());
        users.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("{} - 关闭连接!", session.getId());
        users.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
