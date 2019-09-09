package com.plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartHrServerApplication {

    public static void mazin(String[] args) {
        SpringApplication.run(SmartHrServerApplication.class, args);
    }

}
