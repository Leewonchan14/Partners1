package com.example.techeerpartners1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TecheerPartners1Application {

    public static void main(String[] args) {
        SpringApplication.run(TecheerPartners1Application.class, args);
    }

}
