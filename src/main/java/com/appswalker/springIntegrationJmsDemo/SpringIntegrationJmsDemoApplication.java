package com.appswalker.springIntegrationJmsDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import sun.security.krb5.internal.crypto.Des;

import javax.jms.Destination;

@SpringBootApplication
@IntegrationComponentScan
public class SpringIntegrationJmsDemoApplication {

	@Autowired
	private javax.jms.ConnectionFactory queueConnectionFactory;
	@Autowired
	private Destination requestDestination;
	@Autowired
	private Destination replyDestination;
	@Autowired
	private MessageConverter jacksonJmsMessageConverter;

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationJmsDemoApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringIntegrationJmsDemoApplication.class);
	}

	@Bean
	public IntegrationFlow requestFlow() {
		return IntegrationFlows.from("requests")
				.handle(Jms.outboundGateway(this.queueConnectionFactory)
						.requestDestination(this.requestDestination)
						.correlationKey("JMSCorrelationID"))
						.get();
	}

	@Bean
	public IntegrationFlow responseFlow() {
		return IntegrationFlows.from(Jms.inboundGateway(this.queueConnectionFactory)
				.destination(this.replyDestination))
				.channel("replies")
				.get();
	}

	@ServiceActivator(inputChannel = "replies")
	public String receive(Order order) {
		System.out.println("receive:" + order);
		return "ok";
	}
}
