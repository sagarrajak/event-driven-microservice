package com.microservice.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {
    private Long initialInterval;
    private Long maxInterval;
    private Double multiplier;
    private Integer maxAttempt;
    private Long sleepTimeMs;
}
