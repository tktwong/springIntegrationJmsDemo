package com.appswalker.springIntegrationJmsDemo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
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
    public String send() throws JMSException {
        Random random = new Random();
        Order order = new Order(System.currentTimeMillis(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                random.nextInt());
        /*
        * TODO adding message channel as sender*/
        outboundGateway.sendOrder(order);
        return "OK";
//        log.info("--------- Sending {} ", order);
//        Shipment shipment = clientGateway.sendAndReceive(order);
//        log.info("--------- Received {} ", shipment);
//        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }
}

