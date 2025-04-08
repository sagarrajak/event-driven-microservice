package com.microservice.demo.elastic.index.client.services;

import com.microservice.demo.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<IndexedObjectInformation> save(List<T> query);
}
