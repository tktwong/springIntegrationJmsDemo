package com.appswalker.springIntegrationJmsDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class SpringIntegrationJmsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationJmsDemoApplication.class, args);
	}
}
