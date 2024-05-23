package org.ipan.awspractice.sqs.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Config
 */
@Configuration
public class Config {
    @Autowired
    public SqsProducerStrategy sqsProducerStrategy;

    @Scheduled(cron = "*/15 * * * * *")
    public void produceMessage() {
        sqsProducerStrategy.sendMessage();
    }
}