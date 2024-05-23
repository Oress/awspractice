package org.ipan.awspractice.sqs.producer;

import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public interface SqsProducerStrategy {
    void sendMessage();
}