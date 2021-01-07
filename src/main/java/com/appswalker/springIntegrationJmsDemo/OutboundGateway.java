package com.appswalker.springIntegrationJmsDemo;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "outboundGateway")
public interface OutboundGateway {
    @Gateway(requestChannel = "requests")
    String sendOrder(final Order order);

//    @Gateway(requestChannel = "replies")
//    String replyOrder(final Order order);
}
