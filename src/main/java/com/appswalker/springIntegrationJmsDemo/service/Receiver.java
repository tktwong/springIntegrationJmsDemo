package com.appswalker.springIntegrationJmsDemo.service;

import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class Receiver {
    public Shipment receiveMessage(@Payload Order order) {
        Shipment shipment = new Shipment(order.getId(), UUID.randomUUID().toString());
        return shipment;
    }
}
