package com.microservice.demo.twitter.to.kafka.listener;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import com.microservice.demo.twitter.to.kafka.transformer.TwitterStatusToAvroTransformer;
import com.microservice.demo.kafka.producer.config.services.KafkaProducers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.v1.Status;
import twitter4j.v1.StatusAdapter;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class TwitterKafkaStatusListener extends StatusAdapter {
    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducers<Long, TwitterAvroModel> kafkaProducers;
    private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;

    public TwitterKafkaStatusListener(
            KafkaConfigData kafkaConfigData,
            KafkaProducers<Long, TwitterAvroModel> kafkaProducers,
            TwitterStatusToAvroTransformer twitterStatusToAvroTransformer
    ) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducers = kafkaProducers;
        this.twitterStatusToAvroTransformer = twitterStatusToAvroTransformer;
    }

    @Override
    public void onStatus(Status status) {
        log.info("Twitter status with text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = this.twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status);
        try {
            this.kafkaProducers.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Twitter status with text {} send to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
    }
}
