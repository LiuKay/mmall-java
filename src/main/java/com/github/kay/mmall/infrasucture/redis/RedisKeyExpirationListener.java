package com.github.kay.mmall.infrasucture.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private final RedisKeyExpirationHandlerManager redisKeyExpirationHandlerManager;

    public RedisKeyExpirationListener(
            RedisMessageListenerContainer listenerContainer,
            RedisKeyExpirationHandlerManager redisKeyExpirationHandlerManager) {
        super(listenerContainer);
        this.redisKeyExpirationHandlerManager = redisKeyExpirationHandlerManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.debug("==> Redis Message:{}", message);

            final String key = new String(message.getBody());
            final List<RedisKeyExpirationHandler> handlers = redisKeyExpirationHandlerManager.getHandlers();
            for (RedisKeyExpirationHandler handler : handlers) {
                if (handler.match(key)) {
                    handler.handle(key);
                }
            }
        } catch (Exception exception) {
            log.error(String.format("Failed to handle expiration key:%s", message), exception);
        }
    }
}
