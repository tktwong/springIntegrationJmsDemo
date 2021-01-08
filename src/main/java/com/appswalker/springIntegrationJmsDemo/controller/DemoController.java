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

    private Order getRandomOrder() {
        Random random = new Random();
        return new Order(System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                random.nextInt());
    }

    @GetMapping(value = "/send")
    public ResponseEntity<Shipment> send() throws JMSException {
        return new ResponseEntity<>(outboundGateway.sendAndReceive(getRandomOrder()), HttpStatus.OK);
    }

    @GetMapping(value = "/asyncQueProduce")
    public ResponseEntity<String> asyncProduce() throws JMSException {
        outboundGateway.asyncSendQue(getRandomOrder());
        return new ResponseEntity<>("produced!", HttpStatus.OK);
    }
}

