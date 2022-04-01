package com.myzuji.study.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * kafka 消费者用例
 *
 * @author shine
 * @date 2020/03/01
 */
public class KafkaConsumerDemo {

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("usage: com.myzuji.study.kafka.KafkaConsumerDemo bootstrap-servers topic-name group-name.");
        }
        Properties props = new Properties();
        props.put("bootstrap.servers", args[0]);
        props.put("group.id", args[2]);
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "earliest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new
            KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(args[1]));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofNanos(200));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}
