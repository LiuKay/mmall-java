package com.github.kay.mmall.infrasucture.redis;

public interface RedisKeyExpirationHandler {

    boolean match(String key);

    void handle(String key);

}
