package com.github.kay.mmall.infrasucture.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.SneakyThrows;

@Configuration
@ConditionalOnProperty(prefix = "mmall.redisson", name = "enabled",havingValue = "true")
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @SneakyThrows
    @Bean
    public RedissonClient getRedissonClient(RedissonProperties redissonProperties) {
        final Config config = Config.fromYAML(new ClassPathResource(redissonProperties.getFileName()).getInputStream());
        return Redisson.create(config);
    }

    @Bean
    public LockService lockService(RedissonClient redissonClient){
        return new LockServiceImpl(redissonClient);
    }

}
