package com.maco.followthebeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.maco.followthebeat.v2"
})public class FollowTheBeatApplication {

    public static void main(String[] args) {
        SpringApplication.run(FollowTheBeatApplication.class, args);
    }
}
