package com.appswalker.springIntegrationJmsDemo.config;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import javax.jms.Destination;
import java.util.Map;

@Configuration
public class RequestReplyConfig {
    @Autowired
    private javax.jms.ConnectionFactory queueConnectionFactory;
    @Autowired
    private Destination requestDestination;
    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Bean
    public IntegrationFlow requestFlow() {
        return IntegrationFlows.from(IntegrationConstant.requests)
                .handle(Jms.outboundGateway(this.queueConnectionFactory)
                        .requestDestination(this.requestDestination)
                                .jmsMessageConverter(this.jacksonJmsMessageConverter)
                        .correlationKey("JMSCorrelationID"), //https://jira.spring.io/browse/INT-3405
                        e->e.requiresReply(true)
                        )
                .get();
    }

    @Bean
    public IntegrationFlow responseFlow() {
        return IntegrationFlows.from(Jms.inboundGateway(this.queueConnectionFactory)
                .destination(this.requestDestination)
                .jmsMessageConverter(this.jacksonJmsMessageConverter))
                .channel(IntegrationConstant.replies)
                .get();
    }

    @ServiceActivator(inputChannel = IntegrationConstant.replies)
    public Shipment sendAndReceive(Order order) {
        System.out.println("receive: " + order);
        return new Shipment(order.getId(), order.getTo());
    }

    @Bean
    public IntegrationFlow errorFlow() {
        return IntegrationFlows.from(IntegrationConstant.errors)
                .handle(new GenericHandler<MessagingException>() {
                    @Override
                    public String handle(MessagingException e, MessageHeaders messageHeaders) {
                        System.out.println(messageHeaders);
                        return "Error";
                    }
                })
                .get();
    }

}
