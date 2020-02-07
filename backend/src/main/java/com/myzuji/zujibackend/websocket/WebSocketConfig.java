package com.myzuji.zujibackend.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * 配置websocket入口
     * 允许访问的域
     * 注册Handler、SockJs支持和拦截器。
     *
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //允许连接的域,只能以http或https开头
        String[] allowsOrigins = {"http://127.0.0.1"};

        //addHandler注册和路由的功能，当客户端发起websocket连接，把/path交给对应的handler处理，而不实现具体的业务逻辑，可以理解为收集和任务分发中心。
        //如果不限时使用"*"号，如果指定了域名，则必须要以http或https开头
        registry.addHandler(chatWebSocketHandler(), "/webSocketIMServer")
            .setAllowedOrigins(allowsOrigins)
            .addInterceptors(myInterceptor());
        registry.addHandler(chatWebSocketHandler(), "/sockjs/webSocketIMServer")
            .setAllowedOrigins(allowsOrigins)
            .addInterceptors(myInterceptor())
            .withSockJS();

    }

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler();
    }

    @Bean
    public WebSocketHandshakeInterceptor myInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }
}
