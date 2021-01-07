package com.appswalker.springIntegrationJmsDemo.config;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.Destination;

@Configuration
public class IntegrationFlowConfig {
    @Autowired
    private javax.jms.ConnectionFactory queueConnectionFactory;
    @Autowired
    private Destination requestDestination;
    @Autowired
    private Destination replyDestination;
    @Autowired
    private MessageConverter jacksonJmsMessageConverter;

    @Bean
    public IntegrationFlow requestFlow() {
        return IntegrationFlows.from("requests")
                .handle(Jms.outboundGateway(this.queueConnectionFactory)
                        .requestDestination(this.requestDestination)
                        .correlationKey("JMSCorrelationID"))
                .get();
    }

    @Bean
    public IntegrationFlow responseFlow() {
        return IntegrationFlows.from(Jms.inboundGateway(this.queueConnectionFactory)
                .destination(this.requestDestination))
                .channel("replies")
                .get();
    }

    @ServiceActivator(inputChannel = "replies")
    public Shipment sendAndReceive(Order order) {
        System.out.println("receive: " + order);
        return new Shipment(order.getId(), order.getTo());
    }
}
