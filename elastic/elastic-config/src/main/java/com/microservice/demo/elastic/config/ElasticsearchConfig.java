package com.microservice.demo.elastic.config;

import com.microservice.demo.config.ElasticConfigData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.support.HttpHeaders;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.microservice.demo.elastic.index.client.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    private final ElasticConfigData clientConfiguration;

    public ElasticsearchConfig(ElasticConfigData clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    @Override
    @Bean(name = "elasticsearchClientConfiguration")
    public ClientConfiguration clientConfiguration() {
        log.info("Elasticsearch Client Configuration, host {}, connect timeout {}, socket timeout {}",
                this.clientConfiguration.getConnectionUrl(), this.clientConfiguration.getConnectTimeoutMs(), this.clientConfiguration.getSocketTimeoutMs());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return ClientConfiguration.builder()
                .connectedTo(this.clientConfiguration.getConnectionUrl())
                .withConnectTimeout(this.clientConfiguration.getConnectTimeoutMs())
                .withSocketTimeout(this.clientConfiguration.getSocketTimeoutMs())
                .withDefaultHeaders(headers)
                .build();
    }


}
