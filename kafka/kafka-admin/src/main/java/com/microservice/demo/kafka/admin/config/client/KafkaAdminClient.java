package com.microservice.demo.kafka.admin.config.client;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.config.RetryConfigData;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaAdminClient {
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final KafkaAdmin kafkaAdmin;
    private final RetryTemplate retryTemplate;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, KafkaAdmin kafkaAdmin, RetryTemplate retryTemplate) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.kafkaAdmin = kafkaAdmin;
        this.retryTemplate = retryTemplate;
    }

    public void createTopic() {

    }
}
