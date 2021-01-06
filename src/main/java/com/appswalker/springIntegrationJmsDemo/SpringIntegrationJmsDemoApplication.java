package com.appswalker.springIntegrationJmsDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.Message;

import javax.jms.Destination;

@SpringBootApplication
@IntegrationComponentScan
public class SpringIntegrationJmsDemoApplication {

	@Autowired
	private javax.jms.ConnectionFactory queueConnectionFactory;
	@Autowired
	private Destination mpiResponseQueue;

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationJmsDemoApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringIntegrationJmsDemoApplication.class);
	}
	@Bean
	public IntegrationFlow jmsReader() {
		return IntegrationFlows
				.from(Jms.messageDrivenChannelAdapter(this.queueConnectionFactory)
						.destination(this.mpiResponseQueue))
				.channel("queueReader")
				.get();
	}

	@ServiceActivator(inputChannel = "queueReader")
	public void Print(Message<?> msg)  {
		System.out.println(msg.getPayload().toString());
	}
}
