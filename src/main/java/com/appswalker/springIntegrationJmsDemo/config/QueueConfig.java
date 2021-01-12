package com.appswalker.springIntegrationJmsDemo.config;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
public class QueueConfig {
    @Autowired
    private ConnectionFactory dpmsServiceQcf;
    @Autowired
    private Destination dpmsServiceQue;
    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Bean
    public IntegrationFlow outboundFlow() {
        return IntegrationFlows
                .from(IntegrationConstant.services)
                .handle(Jms.outboundAdapter(this.dpmsServiceQcf).destination(this.dpmsServiceQue))
                .get();
    }

    @Bean
    public IntegrationFlow dpmsServiceQueReader() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(this.dpmsServiceQcf)
                        .destination(this.dpmsServiceQue)
                ).handle(new GenericHandler<Order>(){
                    @Override
                    public Object handle(Order order, MessageHeaders messageHeaders) {
                        System.out.println(order);
                        return null;
                    }
                })
                .get();
    }

}
