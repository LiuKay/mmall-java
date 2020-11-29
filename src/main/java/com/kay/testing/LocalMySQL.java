package com.kay.testing;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MySQLContainer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("local")
public class LocalMySQL {

    private final MySQLContainer mySQLContainer;

    public LocalMySQL() {
        mySQLContainer = new MySQLContainer<>()
                .withInitScript("sql/init.sql")
                .withDatabaseName("mmall")
                .withUsername("test")
                .withPassword("lk123456");
    }

    @PostConstruct
    void start() {
        if (!mySQLContainer.isRunning()) {
            mySQLContainer.start();
            log.info("MySQL is starting at:{}", mySQLContainer.getJdbcUrl());
        }
    }

    @PreDestroy
    void stop() {
        if (mySQLContainer.isRunning()) {
            mySQLContainer.stop();
            log.info("MySQL is stoped.");
        }
    }

    public MySQLContainer getMySQLContainer() {
        return mySQLContainer;
    }
}
