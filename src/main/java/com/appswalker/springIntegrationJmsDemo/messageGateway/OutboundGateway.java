package com.appswalker.springIntegrationJmsDemo.messageGateway;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "outboundGateway")
public interface OutboundGateway {
    @Gateway(requestChannel = "requests")
    Shipment sendAndReceive(final Order order);
}
