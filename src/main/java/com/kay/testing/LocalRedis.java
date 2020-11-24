package com.kay.testing;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;

@Component
@Profile("local")
public class LocalRedis {
    private static final String REDIS_URL_PATTERN = "redis://%s:%d";
    private static final String DOCKER_IMAGE_NAME = "redis:latest";
    private static final int DEFAULT_PORT = 6379;

    private final GenericContainer<?> redisContainer;

    public LocalRedis() {
        redisContainer = new GenericContainer<>(DOCKER_IMAGE_NAME);
        redisContainer.withExposedPorts(DEFAULT_PORT);
    }

    @PostConstruct
    void start() {
        if (!redisContainer.isRunning()) {
            redisContainer.start();
        }
    }

    @PreDestroy
    void stop() {
        if (redisContainer.isRunning()) {
            redisContainer.stop();
        }
    }

    public String getHost() {
        return redisContainer.getHost();
    }

    public int getPort() {
        return redisContainer.getFirstMappedPort();
    }

    public String getUrl() {
        return String.format(REDIS_URL_PATTERN, redisContainer.getHost(), redisContainer.getFirstMappedPort());
    }
}
