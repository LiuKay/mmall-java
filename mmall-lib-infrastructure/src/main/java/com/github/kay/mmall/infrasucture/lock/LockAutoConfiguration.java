package com.github.kay.mmall.infrasucture.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import lombok.SneakyThrows;

@Configuration
public class LockAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "mmall.redisson", name = "enabled",havingValue = "true")
    @EnableConfigurationProperties(RedissonProperties.class)
    static class RedissonLockAutoConfiguration{

        @SneakyThrows
        @Bean
        public RedissonClient getRedissonClient(RedissonProperties redissonProperties) {
            final Config config = Config.fromYAML(new ClassPathResource(redissonProperties.getFileName()).getInputStream());
            return Redisson.create(config);
        }

        @Bean
        @Primary
        public LockService lockService(RedissonClient redissonClient){
            return new LockServiceWithRedisson(redissonClient);
        }
    }

    @Bean
    @ConditionalOnMissingBean(LockService.class)
    public LockService lockService(){
        return new LockServiceDefault();
    }

}
