package org.ipan.awspractice.sqs.producer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestingSqsProducerStrategy implements SqsProducerStrategy {
    private String message;

    @Override
    public void sendMessage() {
        System.out.println(message);
    }
    
}