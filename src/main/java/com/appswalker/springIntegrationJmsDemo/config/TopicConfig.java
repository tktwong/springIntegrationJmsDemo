package com.appswalker.springIntegrationJmsDemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
public class TopicConfig {
    @Autowired
    private ConnectionFactory dpmsServiceTcf;
    @Autowired
    private Destination dpmsServiceTopic;

    @Bean
    public IntegrationFlow pubFlow() {
        return IntegrationFlows
                .from(IntegrationConstant.publishes)
                .handle(Jms.outboundAdapter(this.dpmsServiceTcf)
                        .destination(this.dpmsServiceTopic))
                .get();
    }

    @Bean
    public IntegrationFlow dpmsServiceTopicReader() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(this.dpmsServiceTcf)
                        .destination(this.dpmsServiceTopic).configureListenerContainer(
                                c -> {
                                    c.sessionTransacted(true);
                                    c.pubSubDomain(true);
                                })
                ).handle(new MessageHandler(){
                    public void handleMessage(Message<?> message) throws MessagingException {
                        System.out.println("Topic: Got Message with Payload " + message.getPayload().toString());
                    }
                })
                .get();
    }
}
