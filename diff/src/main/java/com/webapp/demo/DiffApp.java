package com.webapp.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootApplication
public class DiffApp {

    public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplicationBuilder(DiffApp.class).build();
		app.run(args);
    	
    }
}

