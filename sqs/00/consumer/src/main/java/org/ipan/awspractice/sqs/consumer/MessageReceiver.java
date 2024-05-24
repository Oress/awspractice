package org.ipan.awspractice.sqs.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Configuration
public class MessageReceiver {
    private final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);

    private final SqsClient sqsClient;
    private final String queueUrl;

    public MessageReceiver(SqsClient sqsClient, @Value(value = "${sqs.queue.name}") String queueName) {
        this.sqsClient = sqsClient;
        GetQueueUrlResponse response = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
        this.queueUrl = response.queueUrl();
    }

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS)
    public void pollMessages() throws InterruptedException {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(5)
                .build();

        ReceiveMessageResponse resp = this.sqsClient.receiveMessage(receiveMessageRequest);

        List<Message> messages = resp.messages();
        logger.info("Received {} messages", messages.size());

        Collection<DeleteMessageBatchRequestEntry> deleteRequestEntries = new ArrayList<>();
        for (Message message : messages) {
            logger.info("Message ID: {}", message.messageId());
            logger.info("Message Body: {}", message.body());
            logger.info("Message Receipt Handle: {}", message.receiptHandle());
            logger.info("Sleeping for 1 second... ");

            Thread.sleep(1000);

            deleteRequestEntries.add(DeleteMessageBatchRequestEntry.builder().id(message.messageId())
                    .receiptHandle(message.receiptHandle()).build());
        }
        if (!deleteRequestEntries.isEmpty()) {
            try {
                logger.info("Sending Delete batch... ");
                sqsClient.deleteMessageBatch(b -> b.queueUrl(queueUrl).entries(deleteRequestEntries));
            } catch (SqsException e) {
                logger.error("Failed to send delete batch ", e);
            }
        }
    }
}
