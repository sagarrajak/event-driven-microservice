package com.microservice.demo.elastic.index.client.services.impl;

import com.microservice.demo.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.microservice.demo.elastic.index.client.services.ElasticIndexClient;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Primary
@Slf4j
public class ElasticIndexClientRepository implements ElasticIndexClient<TwitterIndexModel> {
    private final TwitterElasticSearchIndexRepository repository;

    public ElasticIndexClientRepository(TwitterElasticSearchIndexRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> query) {
        Iterable<TwitterIndexModel> twitterIndexModels = repository.saveAll(query);
        return StreamSupport.stream(twitterIndexModels.spliterator(), false)
                .map(TwitterIndexModel::getId)
                .collect(Collectors.toList());
    }
}
