package com.example.testemployee.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.example.testemployee")
public class ApplicationProperties {
    private String adminUsername;
    private String adminPassword;
    private String publicUsername;
    private String publicPassword;
    private String cronUsername;
    private String cronPassword;
}
