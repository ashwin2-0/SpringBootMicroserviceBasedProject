package com.lcwd.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // <--- THIS IS THE TRIGGER
public class UserserviceApplication {

    private static final Logger logger = LoggerFactory.getLogger(UserserviceApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);

        logger.info("----------------------------------------------");
        logger.info("   User Service Started Successfully! 🚀     ");
        logger.info("   Port: 8081 | Health: OK                    ");
        logger.info("----------------------------------------------");
    }
}
