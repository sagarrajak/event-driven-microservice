package com.microservice.demo.services.impl;

import com.microservice.demo.config.ElasticConfigData;
import com.microservice.demo.config.ElasticQueryConfigData;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.services.ElasticQueryClient;
import com.microservice.demo.utils.ElasticQueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {
	private static final Logger log = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

	private final ElasticConfigData elasticConfigData;
	private final ElasticQueryConfigData elasticQueryConfigData;
	private final ElasticsearchOperations elasticsearchOperations;
	private final ElasticQueryUtils<TwitterIndexModel> elasticQueryUtil;

	public TwitterElasticQueryClient(ElasticConfigData elasticConfigData,
	                                 ElasticQueryConfigData elasticQueryConfigData,
	                                 ElasticsearchOperations elasticsearchOperations,
	                                 ElasticQueryUtils<TwitterIndexModel> twitterIndexModelElasticQueryUtils
	) {
		this.elasticConfigData = elasticConfigData;
		this.elasticQueryConfigData = elasticQueryConfigData;
		this.elasticsearchOperations = elasticsearchOperations;
		this.elasticQueryUtil = twitterIndexModelElasticQueryUtils;
	}

	@Override
	public TwitterIndexModel getIndexModelById(String id) {
		Query searchQueryById = this.elasticQueryUtil.getSearchQueryById(id);
		SearchHit<TwitterIndexModel> searchedResult = this.elasticsearchOperations.searchOne(searchQueryById,
				TwitterIndexModel.class,
				IndexCoordinates.of(elasticConfigData.getIndexName()));
//		Optional.ofNullable(searchedResult).orElseThrow(() -> {
//		});
		return null;
	}

	@Override
	public List<TwitterIndexModel> getIndexModelByText(String text) {
		return List.of();
	}

	@Override
	public List<TwitterIndexModel> getAllIndexModel() {
		return List.of();
	}
}
