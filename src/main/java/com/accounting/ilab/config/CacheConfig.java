package com.accounting.ilab.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("map-products-cache");
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(1, java.util.concurrent.TimeUnit.DAYS));
        return cacheManager;
    }
}
