package com.appswalker.springIntegrationJmsDemo.config;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import com.appswalker.springIntegrationJmsDemo.service.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
public class RequestReplyConfig {
    @Autowired
    private javax.jms.ConnectionFactory queueConnectionFactory;
    @Autowired
    private Destination dpmsRequestQue;
    @Autowired
    private MessageConverter jacksonJmsMessageConverter;
    @Autowired
    private Receiver receiver;

    @Bean
    public MessageChannel requests() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = IntegrationConstant.requests)
    public JmsOutboundGateway jmsGateway(ConnectionFactory queueConnectionFactory) {
        JmsOutboundGateway gateway = new JmsOutboundGateway();
        gateway.setConnectionFactory(queueConnectionFactory);
        gateway.setRequestDestination(this.dpmsRequestQue);
        gateway.setCorrelationKey("JMSCorrelationID");
        gateway.setSendTimeout(100L);
        gateway.setReceiveTimeout(100L);
        gateway.setDeliveryPersistent(false);
        return gateway;
    }

    @Bean
    public DefaultMessageListenerContainer responder(ConnectionFactory queueConnectionFactory) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(queueConnectionFactory);
        container.setDestination(this.dpmsRequestQue);
        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
            @SuppressWarnings("unused")
            public Shipment handleMessage(Order order) {
                return receiver.receiveMessage(order);
            }
        });
        container.setMessageListener(adapter);
        return container;
    }
}
