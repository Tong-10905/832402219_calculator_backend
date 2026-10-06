package com.fzu.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 * The @SpringBootApplication annotation enables component scanning and auto-configuration,
 * so the whole application can be started with one run of the main method.
 */
@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
