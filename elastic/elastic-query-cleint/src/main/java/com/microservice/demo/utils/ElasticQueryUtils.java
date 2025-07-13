package com.microservice.demo.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import com.microservice.demo.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class ElasticQueryUtils<T extends IndexModel> {
	public Query getSearchQueryById(String id) {
		return NativeQuery.builder().withIds(id).build();

	}

	public Query getSearchQueryByFieldText(String field, String text) {
		return NativeQuery.builder().withQuery(q -> q.bool(b -> b.must(m -> m.match(x -> x.field(field).query(text))))).build();
	}

	public Query getSearchQueryForAll() {
		return NativeQuery.builder().withQuery(q -> q.bool(w -> w.must(x -> x.matchAll(MatchAllQuery.of(ma -> ma))))).build();
	}
}
