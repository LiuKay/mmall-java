package com.kay.config;

import com.kay.util.TimestampProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public TimestampProvider timestampProvider() {
        return new TimestampProvider();
    }

}
