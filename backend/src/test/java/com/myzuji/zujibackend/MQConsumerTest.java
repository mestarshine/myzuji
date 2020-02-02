package com.myzuji.zujibackend;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
public class MQConsumerTest {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer");
        consumer.setNamesrvAddr("192.168.1.250:9876");
        consumer.subscribe("SettleResult", "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.println(Thread.currentThread().getName()
                    + " Receive New Messages: " + msgs);
            MessageExt msg = msgs.get(0);
            MessageListener listener;
            if ("SettleResult".equals(msg.getTopic())) {
                try {
                    String json = new String(msg.getBody(), "UTF-8");
                    System.out.println(LocalTime.now()+":"+json);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    System.out.println("系统异常,希望稍后再试" + e.getMessage());
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } else if ("ApplyShareProfit".equals(msg.getTopic())) {
                String json = null;
                try {
                    json = new String(msg.getBody(), "UTF-8");
                    System.out.println(LocalTime.now()+":"+json);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}

