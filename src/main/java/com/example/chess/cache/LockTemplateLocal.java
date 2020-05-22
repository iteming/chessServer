package com.example.chess.cache;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LockTemplateLocal implements LockTemplate {

    private static LoadingCache<String, String> loadingCache;

    private static Lock lock = new ReentrantLock();

    public LockTemplateLocal() {
        loadingCache = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(100)
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100000)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, String> removalNotification) {
                        log.debug("---{}锁过期---", removalNotification.getKey());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        log.debug("---{}获得锁---", key);
                        return "";
                    }
                });
    }

    @Override
    public boolean lock(String lockKey, String requestId, int expire) {
        try {
            lock.lock();
            String cacheEntity = loadingCache.getIfPresent(lockKey);
            if ("".equals(cacheEntity)) {
                loadingCache.put(lockKey, requestId);
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public void unlock(String lockKey, String requestId) {
        String cacheEntity = loadingCache.getIfPresent(lockKey);
        if (cacheEntity != null && cacheEntity.equals(requestId)) {
            loadingCache.invalidate(lockKey);
        }
    }
}
