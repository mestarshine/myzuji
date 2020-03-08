package com.myzuji.study.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * kafka 生产者用例
 *
 * @author shine
 * @date 2020/03/01
 */
public class KafkaProducerDemo {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        if (args.length != 2) {
            throw new IllegalArgumentException("usage: com.myzuji.study.kafka.KafkaProducerDemo bootstrap-servers topic-name.");
        }
        Properties props = new Properties();
        props.put("bootstrap.servers", args[0]);

        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
            "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
            "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            Future<RecordMetadata> result =
                producer.send(new ProducerRecord<String, String>(args[1],
                    Integer.toString(i), Integer.toString(i)));
            RecordMetadata rm = result.get();
            System.out.println("topic: " + rm.topic() + ", partition: " + rm.partition() + ", offset: " + rm.offset());
        }
        producer.close();
    }

}
