package com.example.chess.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LocalCacheConfig {

    @Value("${id-entity.strLength:4}")
    private int strLength = 4;
    @Value("${id-entity.minCount:1000}")
    private int minCount = 10000;
    @Value("${id-entity.maxCount:300000}")
    private int maxCount = 50000;

    @Bean
    public CacheTemplate localCacheTemplate() {
        return new CacheTemplateLocal(new DefaultCacheNotice());
    }

    @Bean
    public LockTemplate localLockTemplate() {
        return new LockTemplateLocal();
    }

    @Bean
    public IdEntityTemplate localIdEntityTemplate() {
        DefaultIdEntityStorage defaultIdEntityStorage = new DefaultIdEntityStorage(strLength, maxCount);
        return new IdEntityTemplateLocal(defaultIdEntityStorage, strLength, minCount, maxCount);
    }
}
