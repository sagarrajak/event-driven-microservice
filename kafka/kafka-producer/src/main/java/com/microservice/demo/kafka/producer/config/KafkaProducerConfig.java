package com.microservice.demo.kafka.producer.config;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.config.KafkaProducerConfigData;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {
	private final KafkaConfigData kafkaConfigData;
	private final KafkaProducerConfigData producerConfigData;

	public KafkaProducerConfig(KafkaConfigData kafkaConfigData, KafkaProducerConfigData configData) {
		log.info("Init kafka producer config");
		this.kafkaConfigData = kafkaConfigData;
		this.producerConfigData = configData;
	}

	@Bean
	Map<String, Object> producerConfig() {
		Map<String, Object> props = new HashMap<>();
//		props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaConfigData.getBootstrapServer());
//		props.put(ProducerConfig.ACKS_CONFIG, this.producerConfigData.getAsks());
//		props.put(ProducerConfig.BATCH_SIZE_CONFIG, this.producerConfigData.getBatchSize());
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.producerConfigData.getKeySerializerClass());
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.producerConfigData.getValueSerializerClass());
//		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.producerConfigData.getCompressionType());
//		props.put(ProducerConfig.LINGER_MS_CONFIG, this.producerConfigData.getLingerMs());
//		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.producerConfigData.getRequestTimeoutMs());
//		props.put(ProducerConfig.RETRIES_CONFIG, this.producerConfigData.getRetryCount());
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServer());
		props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerConfigData.getKeySerializerClass());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerConfigData.getValueSerializerClass());
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, producerConfigData.getBatchSize() *
				producerConfigData.getBatchSizeBoostFactor());
		props.put(ProducerConfig.LINGER_MS_CONFIG, producerConfigData.getLingerMs());
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerConfigData.getCompressionType());
		props.put(ProducerConfig.ACKS_CONFIG, producerConfigData.getAcks());
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerConfigData.getRequestTimeoutMs());
		props.put(ProducerConfig.RETRIES_CONFIG, producerConfigData.getRetryCount());
		return props;
	}

	@Bean
	ProducerFactory<K, V> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfig());
	}

	@Bean
	KafkaTemplate<K, V> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
