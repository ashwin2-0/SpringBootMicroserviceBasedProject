package com.lcwd.hotel.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class HotelServiceApplication implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(HotelServiceApplication.class);

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);

        logger.info("***********************************************");
        logger.info("   HOTEL SERVICE STARTED SUCCESSFULLY ON 8082  ");
        logger.info("***********************************************");
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("DATABASE CONNECTION VERIFIED: SUCCESS ✅");
            logger.info("Connected to: " + connection.getMetaData().getDatabaseProductName());
            logger.info("DB URL: " + connection.getMetaData().getURL());
        } catch (Exception e) {
            logger.error("DATABASE CONNECTION FAILED: ❌ " + e.getMessage());
        }
    }

}
