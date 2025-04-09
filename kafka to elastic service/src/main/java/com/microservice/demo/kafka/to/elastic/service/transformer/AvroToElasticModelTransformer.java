package com.microservice.demo.kafka.to.elastic.service.transformer;


import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElasticModelTransformer {
    public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> avroModelList) {
        return avroModelList.stream().map(s -> TwitterIndexModel.builder()
                .id(String.valueOf(s.getId()))
                .text(s.getText())
                .userId(s.getUserId())
                .createdAt(ZonedDateTime.ofInstant(Instant.ofEpochMilli(s.getCreatedAt()), ZoneOffset.UTC))
                .build())
                .collect(Collectors.toList());
    }
}
