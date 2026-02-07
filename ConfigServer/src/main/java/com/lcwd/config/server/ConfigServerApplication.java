package com.lcwd.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer  // this annotation is used to enable the config server functionality in this spring boot application
@SpringBootApplication
public class ConfigServerApplication {
	/*
		what is the use of this config server project application?
		this project will act as a config server for all other microservices
		means this project will provide the configuration properties to all other microservices
		when any microservice will start then it will contact to this config server and get its configuration properties

		OR I WILL SAY

		The Spring Cloud Config Server acts as the "Central Brain" or "Librarian" for your
		entire system. In a microservices project like yours, its job is to manage every single .yml
		or .properties file from one central location (GitHub) and hand them out to the right services
		at the right time.

		Think of it this way: instead of each microservice carrying its own heavy
		"instruction manual" (local application.yml), they all call the Librarian (Config Server)
		 when they wake up and ask, "What are my rules for today?"
	 */

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}


