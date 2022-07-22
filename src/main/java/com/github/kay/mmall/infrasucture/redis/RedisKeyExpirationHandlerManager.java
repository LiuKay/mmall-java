package com.github.kay.mmall.infrasucture.redis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RedisKeyExpirationHandlerManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final List<RedisKeyExpirationHandler> handlers = new ArrayList<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init(){
        final Map<String, RedisKeyExpirationHandler> beans = applicationContext.getBeansOfType(
                RedisKeyExpirationHandler.class);
        for (RedisKeyExpirationHandler handler : beans.values()) {
            Objects.requireNonNull(handler);
            handlers.add(handler);
        }
    }

    public List<RedisKeyExpirationHandler> getHandlers() {
        return handlers;
    }
}
