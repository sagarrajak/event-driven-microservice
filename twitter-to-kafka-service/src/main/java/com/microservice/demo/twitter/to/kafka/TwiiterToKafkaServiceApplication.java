package com.microservice.demo.twitter.to.kafka;

import com.microservice.demo.twitter.to.kafka.config.TwitterToKafkaConfigData;
import com.microservice.demo.twitter.to.kafka.runner.StreamRunner;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class TwiiterToKafkaServiceApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(TwiiterToKafkaServiceApplication.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final StreamRunner streamRunner;

    public static void main(String[] args) {
        SpringApplication.run(TwiiterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(twitterToKafkaConfigData.getTwitterKeywords().toString());
        streamRunner.start();
    }
}
