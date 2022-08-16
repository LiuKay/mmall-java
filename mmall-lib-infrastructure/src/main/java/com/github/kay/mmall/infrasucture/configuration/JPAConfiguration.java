package com.github.kay.mmall.infrasucture.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.github.kay.mmall")
public class JPAConfiguration {
}
