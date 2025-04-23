package com.maco.followthebeat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "concerts.api")
public class ConcertsApiConfig{
    private String key;
    private String host;
    private String baseUrl;
}
