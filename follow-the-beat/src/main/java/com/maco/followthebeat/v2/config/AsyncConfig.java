package com.maco.followthebeat.v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "spotifyTaskExecutor")
    public Executor spotifyTaskExecutor() {
        return Executors.newFixedThreadPool(100); // adjust thread pool size as needed
    }
}
