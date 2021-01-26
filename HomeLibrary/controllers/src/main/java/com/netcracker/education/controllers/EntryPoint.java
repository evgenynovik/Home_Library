package com.netcracker.education.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Evgeny Novik
 */
@Slf4j
@EnableJpaRepositories(basePackages = "com.netcracker.education.dao.interfaces")
@EntityScan(basePackages = "com.netcracker.education.dao.domain")
@SpringBootApplication(scanBasePackages = "com.netcracker.education")
public class EntryPoint {

    public static void main(String[] args) {
        SpringApplication.run(EntryPoint.class, args);
        log.info(" Application is launched.");
    }
}