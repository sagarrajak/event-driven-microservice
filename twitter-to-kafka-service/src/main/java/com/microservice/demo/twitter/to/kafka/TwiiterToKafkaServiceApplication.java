package com.microservice.demo.twitter.to.kafka;

import com.microservice.demo.config.TwitterToKafkaConfigData;
import com.microservice.demo.twitter.to.kafka.init.StreamInitializer;
import com.microservice.demo.twitter.to.kafka.runner.StreamRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.microservice.demo")
@AllArgsConstructor
@Slf4j
public class TwiiterToKafkaServiceApplication implements CommandLineRunner {

    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;
    public static void main(String[] args) {
        SpringApplication.run(TwiiterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamRunner.start();
        streamInitializer.init();
    }
}
