package com.appswalker.springIntegrationJmsDemo.message;

import com.appswalker.springIntegrationJmsDemo.config.IntegrationConstant;
import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "outboundGateway", errorChannel = IntegrationConstant.errors)
public interface OutboundGateway {
    @Gateway(requestChannel = IntegrationConstant.requests)
    Shipment sendAndReceive(final Message<Order> message);

    @Gateway(requestChannel = IntegrationConstant.services)
    void asyncSendQue(final Order order);

    @Gateway(requestChannel = IntegrationConstant.publishes)
    void publish(final Order order);
}
