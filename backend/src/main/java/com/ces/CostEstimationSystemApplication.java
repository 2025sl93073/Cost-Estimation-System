package com.ces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CostEstimationSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CostEstimationSystemApplication.class, args);
        
        System.out.println("Cost Estimation System is running...");
    }
}
