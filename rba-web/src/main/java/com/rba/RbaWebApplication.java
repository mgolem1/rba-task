package com.rba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = "com.rba")
@EntityScan(basePackages = "com.rba")
@EnableJpaRepositories(basePackages = "com.rba")
@EnableKafka
public class RbaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbaWebApplication.class, args);
    }
}