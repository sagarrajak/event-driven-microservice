package com.microservice.demo.elastic.index.client.util;

import com.microservice.demo.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticIndexUtil<T extends IndexModel> {
    public List<IndexQuery> getIndexQuery(List<T> documents) {
        return documents.stream()
                    .map(s -> new IndexQueryBuilder()
                            .withId(s.getId())
                            .withObject(s)
                            .build()
                    )
                .collect(Collectors.toList());
    }
}
