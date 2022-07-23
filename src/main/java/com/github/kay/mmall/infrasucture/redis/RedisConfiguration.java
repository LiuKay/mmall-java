package com.github.kay.mmall.infrasucture.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisConfigurationProperties.class)
public class RedisConfiguration {

    private static final String URL = "redis://%s:%s";

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient getRedissonClient(RedisConfigurationProperties redisConfigurationProperties) {
        Config config = new Config();
        config.setLockWatchdogTimeout(redisConfigurationProperties.getLockWatchdogTimeout())
              .useSingleServer()
              .setAddress(String.format(URL, redisConfigurationProperties.getHost(),
                                        redisConfigurationProperties.getPort()))
              .setConnectionPoolSize(redisConfigurationProperties.getConnectionPoolSize())
              .setConnectTimeout(redisConfigurationProperties.getConnectionTimeout())
              .setTimeout(redisConfigurationProperties.getTimeout())
              .setRetryAttempts(redisConfigurationProperties.getRetryAttempts())
              .setRetryInterval(redisConfigurationProperties.getRetryInterval())
              .setSslEnableEndpointIdentification(redisConfigurationProperties.isSslEnableEndpointIdentification())
              .setKeepAlive(true);

        return Redisson.create(config);
    }


    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisConfigurationProperties redisConfigurationProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
                redisConfigurationProperties.getHost(),
                redisConfigurationProperties.getPort());
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }


}
