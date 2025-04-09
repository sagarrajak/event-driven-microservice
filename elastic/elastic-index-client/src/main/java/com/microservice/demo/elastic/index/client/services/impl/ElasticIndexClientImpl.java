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
    public List<IndexedObjectInformation> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQuery = elasticIndexUtill.getIndexQuery(documents);
        indexQuery.forEach(query -> {
            log.info("indexQuery: {}", query.toString());
        });

        try {
            List<IndexedObjectInformation> indexedTweets = this.elasticsearchOperations.bulkIndex(indexQuery,
                    TwitterIndexModel.class);
            return indexedTweets;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }

//        log.info("indexedTweets: {}", indexedTweets.stream().map(s -> s.id()).collect(Collectors.toList()));
//        indexedTweets.forEach(s -> {
//            System.out.println(s.id()+" saved to db");
//        });
    }
}
