package com.microservice.demo.twitter.to.kafka.runner.impl;

import com.microservice.demo.config.TwitterToKafkaConfigData;
import com.microservice.demo.twitter.to.kafka.listener.TwitterKafkaStatusListener;
import com.microservice.demo.twitter.to.kafka.runner.StreamRunner;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.v1.Status;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;


@Component
@RequiredArgsConstructor
public class MockKafkaStreamRunner implements StreamRunner {
    private static final Logger log = LoggerFactory.getLogger(MockKafkaStreamRunner.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final TwitterKafkaStatusListener listener;

    private static final Random random = new Random();
    private static final String[] WORDS = new String[]{
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor",
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero"
    };

    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";


    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM d HH:mm:ss Z yyyy";

    @Override
    public void start() throws TwitterException {
        final String[] keywords = twitterToKafkaConfigData.getTwitterKeywords().toArray(new String[0]);
        long sleepTimeMs = twitterToKafkaConfigData.getMockSleepMs();
        log.info("Starting mock filtering twitter streams for keywords {}", Arrays.toString(keywords));
        simulateTwitterStream(keywords,  sleepTimeMs);
    }

    private void simulateTwitterStream(String[] keywords, long sleepTimeMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formattedTweet = getFormattedTweet(keywords);
                    Status status = TwitterObjectFactory.createStatus(formattedTweet);
                    listener.onStatus(status);
                    System.out.println(status);
                    sleep(sleepTimeMs);
                }
            } catch (TwitterException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        });
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedTweet(String[] keywords) {
        String[] params = new String[]{
                ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomTweetContent(keywords),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        };
        return formatTweetAsJsonWithParams(params);
    }

    private String formatTweetAsJsonWithParams(String[] params) {
        String tweet = tweetAsRawJson;

        for (int i = 0; i < params.length; i++) {
            tweet = tweet.replace("{" + i + "}", params[i]);
        }
        return tweet;
    }

    private String getRandomTweetContent(String[] keywords) {
        StringBuilder tweet = new StringBuilder();
        int tweetLength = random.nextInt(10 - 5 + 1) + 5;
        for (int i = 0; i < tweetLength; i++) {
            tweet.append(WORDS[random.nextInt(WORDS.length)]).append(" ");
            if (i == tweetLength / 2) {
                tweet.append(keywords[random.nextInt(keywords.length)]).append(" ");
            }
        }
        return tweet.toString().trim();
    }
}
