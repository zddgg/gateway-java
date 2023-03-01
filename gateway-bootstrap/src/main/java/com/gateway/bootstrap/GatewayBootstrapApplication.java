package com.gateway.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.gateway")
@SpringBootApplication
public class GatewayBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayBootstrapApplication.class, args);
    }
}
