package com.microservice.demo.services;

import com.microservice.demo.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {
	T getIndexModelById(String id);
	List<T> getIndexModelByText(String text);
	List<T> getAllIndexModel();
}
