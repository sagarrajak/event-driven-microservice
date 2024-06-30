package com.microservice.demo.twitter.to.kafka.init.impl;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.kafka.admin.config.client.KafkaAdminClient;
import com.microservice.demo.twitter.to.kafka.init.StreamInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class KafkaStreamInitializer implements StreamInitializer {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        this.kafkaAdminClient.createTopic();
        this.kafkaAdminClient.checkSchemaRegistryUpAndRunning();
        log.info("topics with name {} is ready for operations", this.kafkaConfigData.getTopicNameToCreate());
    }
}
