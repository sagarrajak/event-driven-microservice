package com.microservice.demo.twitter.to.kafka.transformer;

import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.v1.Status;

import java.time.ZoneOffset;

@Component
public class TwitterStatusToAvroTransformer {
    public TwitterAvroModel getTwitterAvroModelFromStatus(Status status) {
        long epochSecond = status.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
        return  TwitterAvroModel
                .newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(epochSecond)
                .build();
    }
}
