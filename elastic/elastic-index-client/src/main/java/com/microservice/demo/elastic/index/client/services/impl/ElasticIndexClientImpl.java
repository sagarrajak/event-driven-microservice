package com.microservice.demo.elastic.index.client.services.impl;

import com.microservice.demo.elastic.index.client.services.ElasticIndexClient;
import com.microservice.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ElasticIndexClientImpl implements ElasticIndexClient<TwitterIndexModel> {
    private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtill;
    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticIndexClientImpl(ElasticIndexUtil<TwitterIndexModel> elasticIndexClient,
                                  ElasticsearchOperations elasticsearchOperations) {
        this.elasticIndexUtill = elasticIndexClient;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<IndexedObjectInformation> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQuery = elasticIndexUtill.getIndexQuery(documents);
        List<IndexedObjectInformation> indexedTweets = this.elasticsearchOperations.bulkIndex(indexQuery,
                TwitterIndexModel.class);
        log.info("indexedTweets: {}", indexedTweets.stream().map(IndexedObjectInformation::id).toArray());
        return indexedTweets;
    }
}
