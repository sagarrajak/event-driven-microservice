package com.microservice.demo.twitter.to.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.v1.Status;
import twitter4j.v1.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {
   private static final Logger log = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);
    @Override
    public void onStatus(Status status) {
       log.info("Twitter status with text {}", status.getText());
    }
}
