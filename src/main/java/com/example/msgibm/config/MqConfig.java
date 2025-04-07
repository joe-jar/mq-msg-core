package com.example.msgibm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

@Configuration
public class MqConfig {

    private static final Logger log = LoggerFactory.getLogger(MqConfig.class);

    @Value("${ibm.mq.listener.concurrency:1-5}")
    private String concurrency;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setErrorHandler(t ->
                log.error("JMS Error: {}", t.getMessage(), t)
        );

        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

        factory.setConcurrency(concurrency); //  Configurable for tuning

        return factory;
    }
}
