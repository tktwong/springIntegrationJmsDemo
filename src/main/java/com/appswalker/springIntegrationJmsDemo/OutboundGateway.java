package com.appswalker.springIntegrationJmsDemo;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "outboundGateway")
public interface OutboundGateway {
    @Gateway(requestChannel = "requests")
    void sendOrder(final Order order);
}
