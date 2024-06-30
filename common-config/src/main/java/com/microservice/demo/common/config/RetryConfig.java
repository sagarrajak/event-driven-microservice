package com.microservice.demo.common.config;

import com.microservice.demo.config.RetryConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
    private final RetryConfigData retryConfigData;

    public RetryConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        // setting back off policy
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(this.retryConfigData.getInitialInterval());
        exponentialBackOffPolicy.setMaxInterval(this.retryConfigData.getMaxInterval());
        exponentialBackOffPolicy.setMultiplier(this.retryConfigData.getMultiplier());
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
        // setting up retry policy
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(this.retryConfigData.getMaxAttempt());
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        return  retryTemplate;
    }
}
