package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * main spring application configuration
 */
@EnableMongoRepositories
@SpringBootApplication
public class Main {

    /**
     * main class
     * @param args : spring application args
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
