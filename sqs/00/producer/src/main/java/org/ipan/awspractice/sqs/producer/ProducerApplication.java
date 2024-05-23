package org.ipan.awspractice.sqs.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootApplication
@EnableScheduling
public class ProducerApplication {

    @Value(value = "${sqs.queue.name}")
    public String sqsName;

    @Value(value = "${sqs.region}")
    public String region;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Bean
    @Profile("testing")
    public SqsProducerStrategy sqsProducerStrategy() {
        return new TestingSqsProducerStrategy("Hello World");
    }

    @Bean
    @Profile("!testing")
    public SqsProducerStrategy prodSqsProducerStrategy(SqsClient sqsClient) {
        return new ProdSqsProducerStrategy(sqsClient, sqsName);
    }

    @Bean
    public SqsClient sqsClient() {
        if (sqsName.isBlank()) {
            // throw initialization exception
            throw new RuntimeException("SQS endpoint is not provided");
        }

        if (region.isBlank()) {
            // throw initialization exception
            throw new RuntimeException("Region is not provided");
        }

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .build();

        return sqsClient;
    }
}
