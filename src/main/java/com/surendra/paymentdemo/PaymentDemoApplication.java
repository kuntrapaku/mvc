package com.surendra.paymentdemo; // root package

import org.springframework.boot.SpringApplication; // starts spring boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // enables auto configuration

@SpringBootApplication // starting point of spring boot
public class PaymentDemoApplication {

    public static void main(String[] args) { // java starts execution from here
        SpringApplication.run(PaymentDemoApplication.class, args); // start spring application
    }
}