package com.myzuji.zujibackend.websocket;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
@Service
public class MessageListening {

    @PostConstruct
    public void start() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("ws-push-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> MessageQueue.getInstance().pushMessage(UUID.randomUUID().toString()),
                1, 1, TimeUnit.SECONDS);
    }
}
