package com.microservice.demo.kafka.admin.config.client;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.kafka.admin.config.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClient {
    private final KafkaConfigData kafkaConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, AdminClient adminClient, RetryTemplate retryTemplate, WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopic() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = this.retryTemplate.execute(this::doCreateTopic);
        } catch (RuntimeException e) {
            throw new KafkaClientException("Reached max number of retry creating kafka topic's!");
        }
    }

    private CreateTopicsResult doCreateTopic(RetryContext retryContext) {
        List<String> topicNameToCreate = this.kafkaConfigData.getTopicNameToCreate();
        List<NewTopic> kafkaTopics = topicNameToCreate.stream().map(name -> new NewTopic(
                name.trim(),
                this.kafkaConfigData.getNumberOfPartitions(),
                this.kafkaConfigData.getReplicationFactor())).collect(Collectors.toList());
        return this.adminClient.createTopics(kafkaTopics);
    }

    public void checkSchemaRegistryUpAndRunning() {
        try {
            this.retryTemplate.execute(this::doCheckSchemaRegistory);
        } catch (RuntimeException e) {
            throw new KafkaClientException("Unable to connect to schema registry", e);
        }
    }

    private HttpStatusCode doCheckSchemaRegistory(RetryContext retryContext) {
        HttpStatusCode code = this.webClient.method(HttpMethod.GET)
                .uri(kafkaConfigData.getSchemaRegistryUrl())
                .exchange()
                .map(ClientResponse::statusCode)
                .block();

        if (code == null || !code.is2xxSuccessful()) {
            throw new RuntimeException("Something went wrong!");
        }
        return code;
    }

    public void checkTopicCreated() {
        Collection<TopicListing> topics = this.getTopics();
        for (String topic: this.kafkaConfigData.getTopicNameToCreate()) {
            try {
                Collection<TopicListing> finalTopics = topics;
                topics = this.retryTemplate.execute((RetryCallback<Collection<TopicListing>, Throwable>) context -> {
                    // check if topic created or not
                    if (!isTopicCreate(finalTopics, topic)) {
                        throw new RuntimeException("Topic not created");
                    }
                    // get topic;
                    return this.getTopics();
                });
            } catch (Throwable e) {}
        }
    }

    private boolean isTopicCreate(Collection<TopicListing> topics, String topic) {
        if (topics == null) return false;
        return topics.stream().anyMatch(localTopic -> localTopic.name().equals(topic));
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> listings;
        try {
            listings = this.retryTemplate.execute(this::doGetTopics);
            return listings;
        } catch (Exception e) {
            throw new KafkaClientException("Reached max number of attempt reading kafka topics", e);
        }
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        return this.adminClient.listTopics().listings().get();
    }
}
