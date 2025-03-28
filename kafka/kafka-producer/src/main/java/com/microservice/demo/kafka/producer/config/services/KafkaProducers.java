package com.microservice.demo.kafka.producer.config.services;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import org.apache.avro.specific.SpecificRecordBase;

public interface KafkaProducers<K extends Serializable, V extends SpecificRecordBase>{
	void send(String topicName, K key, V value) throws ExecutionException, InterruptedException;
}
