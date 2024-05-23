package org.ipan.awspractice.sqs.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

public class ProdSqsProducerStrategy implements SqsProducerStrategy {
    private final SqsClient sqsClient;
    private final String queueUrl;

    public ProdSqsProducerStrategy(SqsClient sqsClient, @Value(value = "sqs.queue.name") String queueName) {
        this.sqsClient = sqsClient;
        GetQueueUrlResponse resp = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
        this.queueUrl = resp.queueUrl();
    }

    @Override
    public void sendMessage() {
        try {
            SendMessageRequest mRequest = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(Long.toString(System.currentTimeMillis())).build();
            sqsClient.sendMessage(mRequest);    
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }
    
}