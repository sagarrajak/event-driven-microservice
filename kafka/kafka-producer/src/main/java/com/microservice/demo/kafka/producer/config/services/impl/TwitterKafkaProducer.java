package com.microservice.demo.kafka.producer.config.services.impl;

import java.util.concurrent.CompletableFuture;

import com.microservice.demo.kafka.producer.config.services.KafkaProducers;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.microservice.demo.kafka.avro.model.TwitterAvroModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TwitterKafkaProducer implements KafkaProducers<Long, TwitterAvroModel> {

    private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate) {
        log.info("Init kafka producer template");
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        log.info("Sending message = '{}' to topic='{}'",  topicName, message );
        CompletableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture =
                this.kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.whenComplete((sendResult, exception) -> {
            ProducerRecord<Long, TwitterAvroModel> producerRecord = sendResult.getProducerRecord();
            log.debug("Received new metadata, Topic: {}, Partition: {}, offset: {}, Timestamp: {}, and time {}",
                        producerRecord.topic(),
                        producerRecord.partition(),
                        "",
                        producerRecord.timestamp(),
                        System.nanoTime()
                    );
        });
    }

    @PreDestroy
    public void onClose() {
        if (this.kafkaTemplate != null) {
            log.info("Closing kafka producer");
            this.kafkaTemplate.destroy();
        }
    }
}
