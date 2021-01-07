package com.appswalker.springIntegrationJmsDemo.controller;

import com.appswalker.springIntegrationJmsDemo.messageGateway.OutboundGateway;
import com.appswalker.springIntegrationJmsDemo.model.Order;
import com.appswalker.springIntegrationJmsDemo.model.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.util.Random;
import java.util.UUID;

@RestController
@Slf4j
public class DemoController {

    @Autowired
    OutboundGateway outboundGateway;

    @GetMapping(value = "/send")
    public ResponseEntity<Shipment> send() throws JMSException {
        Random random = new Random();
        Order order = new Order(System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                random.nextInt());
        return new ResponseEntity<>(outboundGateway.sendAndReceive(order), HttpStatus.OK);
    }
}

