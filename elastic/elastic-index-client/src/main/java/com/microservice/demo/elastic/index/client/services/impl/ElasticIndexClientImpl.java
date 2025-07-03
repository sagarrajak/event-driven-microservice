package com.microservice.demo.elastic.index.client.services.impl;

import com.microservice.demo.config.ElasticConfigData;
import com.microservice.demo.elastic.index.client.services.ElasticIndexClient;
import com.microservice.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticIndexClientImpl implements ElasticIndexClient<TwitterIndexModel> {
    private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtill;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticConfigData elasticConfigData;
    private final ClientConfiguration clientConfiguration;

    public ElasticIndexClientImpl(ElasticIndexUtil<TwitterIndexModel> elasticIndexClient,
                                  ElasticsearchOperations elasticsearchOperations, ElasticConfigData elasticConfigData, ClientConfiguration clientConfiguration
    ) {
        this.elasticIndexUtill = elasticIndexClient;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticConfigData = elasticConfigData;
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQuery = elasticIndexUtill.getIndexQuery(documents);
        indexQuery.forEach(query -> {
            log.info("indexQuery: {}", query.toString());
        });

        try {
            List<IndexedObjectInformation> indexedTweets = this.elasticsearchOperations.bulkIndex(indexQuery,
                    TwitterIndexModel.class);
            return indexedTweets.stream().map(IndexedObjectInformation::id).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }
}
