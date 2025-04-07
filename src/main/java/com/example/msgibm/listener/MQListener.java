package com.example.msgibm.listener;

import com.example.msgibm.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MQListener {

    @Autowired
    private MessageService messageService;

    private final JmsTemplate jmsTemplate;

    @Value("${ibm.mq.queue}")
    private String sourceQueue;

    @Value("${ibm.mq.backoutQueue:}")
    private String backoutQueue;

    @Value("${ibm.mq.backoutThreshold:3}")
    private int backoutThreshold;

    // listening to incoming messages
    //redelivery and backout included
    @JmsListener(destination = "${ibm.mq.queue}", containerFactory = "jmsListenerContainerFactory")
    public void receive(javax.jms.Message message) {
        try {
            String body = ((javax.jms.TextMessage) message).getText();
            messageService.saveMessage(body, sourceQueue);

        } catch (Exception e) {
            System.err.println("Failed to process message. Checking for redelivery...");

            try {
                int deliveryCount = message.getIntProperty("JMSXDeliveryCount");

                if (deliveryCount >= backoutThreshold && StringUtils.hasText(backoutQueue)) {
                    String body = ((javax.jms.TextMessage) message).getText();
                    System.err.printf("Redirecting message to backout queue [%s] after %d attempts%n", backoutQueue, deliveryCount);
                    jmsTemplate.convertAndSend(backoutQueue, body);
                } else {
                    throw new RuntimeException("Processing failed, message will be retried.");
                }

            } catch (Exception ex) {
                System.err.println("Failed to handle redelivery/backout: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }
}
