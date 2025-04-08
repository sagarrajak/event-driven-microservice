package com.microservice.demo.elastic.config;

import com.microservice.demo.config.ElasticConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    private final ElasticConfigData clientConfiguration;

    public ElasticsearchConfig(ElasticConfigData clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(this.clientConfiguration.getConnectionUrl())
                .withConnectTimeout(this.clientConfiguration.getConnectTimeoutMs())
                .withSocketTimeout(this.clientConfiguration.getSocketTimeoutMs())
                .build();
    }
}
