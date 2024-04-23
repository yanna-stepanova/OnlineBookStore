package com.yanna.stepanova.config;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Random getMyRandom() {
        return new Random();
    }
}
