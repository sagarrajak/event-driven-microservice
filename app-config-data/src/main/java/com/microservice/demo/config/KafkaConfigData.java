package com.microservice.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kafka-config")
@Data
public class KafkaConfigData {
    String bootstrapServer;
    String schemaRegistryUrlKey;
    String schemaRegistryUrl;
    String topicName;
    List<String> topicNameToCreate;
    Integer numberOfPartitions;
    Short replicationFactor;
}
