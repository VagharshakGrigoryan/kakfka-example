package com.example.kakfkaexample.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(Producer.class);
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 10000; i++) {
            ProducerRecord<Integer, String> record = new ProducerRecord<>("demo-topic1", i % 3, "" + i);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    logger.info("received new metadata, topic " + getMetadata(metadata).topic() + " partition " +
                            getMetadata(metadata).partition() + " offsets " + getMetadata(metadata).offset() + " time " + getMetadata(metadata).timestamp());
                } else {
                    logger.error("error producing", exception);
                }
            });
            Thread.sleep(1000);
        }
        producer.flush();
        producer.close();
    }

    private static RecordMetadata getMetadata(RecordMetadata metadata) {
        return metadata;
    }

}

