package com.example.franchise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.franchise")
public class FranchiseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchiseServiceApplication.class, args);
    }
}
