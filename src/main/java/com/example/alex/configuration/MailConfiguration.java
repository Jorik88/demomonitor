package com.example.alex.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.report.mail")
public class MailConfiguration {

    private String host;
    private String username;
    private String protocol;
    private String password;
    private int port;
}
