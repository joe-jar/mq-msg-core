package com.example.mqlistenerdemo.listener;

import com.example.mqlistenerdemo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MQListener {

    private final MessageService messageService;

    @JmsListener(destination = "${ibm.mq.queue}")
    public void receive(String message) {
        System.out.println("✅ Received message: " + message);
        messageService.saveMessage(message, "DEV.QUEUE.1"); // You could make the queue name dynamic if needed
    }
}
