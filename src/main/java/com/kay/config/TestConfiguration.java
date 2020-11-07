package com.kay.config;

import com.kay.testing.LocalRedis;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Profile("local")
@Configuration
@ConditionalOnProperty(name = "application.test.enabled")
public class TestConfiguration {

    @Autowired
    private TestConfiguration testConfiguration;

    @Bean
    public RedisConnectionFactory localRedisConnectionFactory(LocalRedis localRedis) {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(localRedis.getHost(), localRedis.getPort()));
    }

    @Data
    @ConfigurationProperties("application.test")
    public static class TestProperties {
        boolean enabled = false;
    }

}
