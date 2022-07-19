package com.github.kay.mmall.infrasucture;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    public static final long SYSTEM_DEFAULT_EXPIRES = 5 * 60 * 1000L;

    @Bean
    public CacheManager configCacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
                                    .expireAfterWrite(SYSTEM_DEFAULT_EXPIRES, TimeUnit.MILLISECONDS));
        return manager;
    }

    //TODO: 支付订单放在了缓存中，没有做持久化
    @Bean(name = "settlement")
    public Cache getSettlementTTLCache() {
        return new CaffeineCache("settlement", Caffeine.newBuilder().expireAfterAccess(SYSTEM_DEFAULT_EXPIRES, TimeUnit.MILLISECONDS).build());
    }
}
