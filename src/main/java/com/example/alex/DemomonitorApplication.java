package com.example.alex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemomonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemomonitorApplication.class, args);
    }

}

