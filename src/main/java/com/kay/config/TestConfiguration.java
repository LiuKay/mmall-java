package com.kay.config;

import com.kay.testing.LocalMySQL;
import com.kay.testing.LocalRedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.sql.DataSource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @Bean
    public DataSource getDataSource(LocalMySQL mysql) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(mysql.getMySQLContainer().getDriverClassName());
        dataSourceBuilder.url(mysql.getMySQLContainer().getJdbcUrl());
        dataSourceBuilder.username(mysql.getMySQLContainer().getUsername());
        dataSourceBuilder.password(mysql.getMySQLContainer().getPassword());
        return dataSourceBuilder.build();
    }

    @Data
    @ConfigurationProperties("application.test")
    public static class TestProperties {
        boolean enabled = false;
    }

}
