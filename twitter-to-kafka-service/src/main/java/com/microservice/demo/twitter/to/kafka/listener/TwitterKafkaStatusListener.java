package com.microservice.demo.twitter.to.kafka.listener;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import com.microservice.demo.twitter.to.kafka.transformer.TwitterStatusToAvroTransformer;
import com.microservice.demo.kafka.producer.config.services.KafkaProducers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.v1.Status;
import twitter4j.v1.StatusAdapter;

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
        this.kafkaProducers.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
    }
}
