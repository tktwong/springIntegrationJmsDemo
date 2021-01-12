package com.appswalker.springIntegrationJmsDemo.message;

import com.appswalker.springIntegrationJmsDemo.config.IntegrationConstant;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.handler.annotation.Header;

@MessageEndpoint
public class OrderRouter {
    @Router(inputChannel= IntegrationConstant.replies)
    public String route(@Header("ORDER_TYPE") String orderType) {
        if ("b2b".equals(orderType)) {
            return "b2b.channel";
        } else if ("b2c".equals(orderType)) {
            return "b2c.channel";
        } else {
            return "nullChannel";
        }
    }
}
