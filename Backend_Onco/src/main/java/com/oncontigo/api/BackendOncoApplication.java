package com.oncontigo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



@SpringBootApplication
@EnableJpaAuditing
public class BackendOncoApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendOncoApplication.class, args);
    }

}
