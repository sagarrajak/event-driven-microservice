package com.microservice.demo.kafka.to.elastic.service.consumer.impl;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.elastic.index.client.services.ElasticIndexClient;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.kafka.admin.config.client.KafkaAdminClient;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import com.microservice.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microservice.demo.kafka.to.elastic.service.transformer.AvroToElasticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.Lifecycle;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {
    private final KafkaListenerEndpointRegistry registry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final AvroToElasticModelTransformer transformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaConsumer.class);

    public TwitterKafkaConsumer(
            KafkaListenerEndpointRegistry registry,
            KafkaAdminClient kafkaAdminClient,
            KafkaConfigData kafkaConfigData,
            AvroToElasticModelTransformer transformer,
            ElasticIndexClient<TwitterIndexModel> elasticIndexClient
    ) {
        this.registry = registry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.transformer = transformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener
    public void onBootstrap(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicCreated();
        log.info("Topic with name {} is created", kafkaConfigData.getTopicNameToCreate().toString());
        Optional.ofNullable(this.registry.getListenerContainer("twitterTopicListener"))
                .ifPresent(Lifecycle::start);
    }

    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(
            @Payload List<TwitterAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        log.info("number of messages: {}, keys {}, partition {}, offset {}", messages.size(), keys.toString(), partitions.toString(), offsets.toString());
        List<TwitterIndexModel> elasticModels = this.transformer.getElasticModels(messages);
        elasticModels.forEach(message -> {
            log.info(message.toString());
        });
        List<String> savedData = this.elasticIndexClient.save(elasticModels);

    }
}
