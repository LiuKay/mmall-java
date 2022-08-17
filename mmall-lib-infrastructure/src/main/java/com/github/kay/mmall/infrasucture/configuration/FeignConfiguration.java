package com.github.kay.mmall.infrasucture.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableFeignClients(basePackages = {"com.github.kay.mmall"})
public class FeignConfiguration {
}
