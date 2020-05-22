package com.example.chess.cache;


import com.google.common.cache.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class IdEntityTemplateLocal implements IdEntityTemplate {

    private static LoadingCache<String, Map<String, AtomicLong>> autoCode;
    private static LoadingCache<String, LinkedList<String>> randomStr;

    private final static int concurrencyLevel = 10;

    private final static long duration = 60 * 60;

    private final static int initialCapacity = 10;

    private final static int size = 1000;

    private final int strLength;
    private final int minCount;
    private final int maxCount;


    public IdEntityTemplateLocal(IdEntityStorage entityStorage, int strLength, int minCount, int maxCount) {
        autoCode = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(concurrencyLevel)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(duration, TimeUnit.SECONDS)
                .expireAfterAccess(duration, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(initialCapacity)
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(size)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(new RemovalListener<String, Map<String, AtomicLong>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Map<String, AtomicLong>> removalNotification) {
                        entityStorage.onRemovalAutoCode(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, Map<String, AtomicLong>>() {
                    @Override
                    public Map<String, AtomicLong> load(String key) throws Exception {
                        return entityStorage.loadAutoCode(key);
                    }
                });


        randomStr = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(concurrencyLevel)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(duration, TimeUnit.SECONDS)
                .expireAfterAccess(duration, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(initialCapacity)
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(size)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(new RemovalListener<String, LinkedList<String>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, LinkedList<String>> removalNotification) {
                        entityStorage.onRemovalRandomStr(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, LinkedList<String>>() {
                    @Override
                    public LinkedList<String> load(String key) throws Exception {
                        return entityStorage.loadRandomStr(key);
                    }
                });
        this.strLength = strLength;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    public Long getAutoLong(String key, String field, Long initialVal) {
        try {
            Map<String, AtomicLong> longMap = autoCode.get(key);
            if (longMap.isEmpty()) {
                longMap = new HashMap<>();
            }
            AtomicLong atomicLong = longMap.get(field);
            if (atomicLong == null) {
                atomicLong = new AtomicLong(initialVal);
            } else {
                atomicLong.incrementAndGet();
            }
            longMap.put(field, atomicLong);
            autoCode.put(key, longMap);
            return atomicLong.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean exists(String key, String field) {
        Map<String, AtomicLong> longMap = autoCode.getUnchecked(key);
        if (longMap == null) {
            return false;
        }
        return longMap.get(field) != null;
    }

    @Override
    public Long createAutoLong(String key, String field, Long initialVal) {
        AtomicLong atomicLong = new AtomicLong(initialVal);
        Map<String, AtomicLong> longMap = new HashMap<>();
        longMap.put(field, atomicLong);
        autoCode.put(key, longMap);
        return atomicLong.get();
    }

    @Override
    public void addRandomStr(String key,int strLength) {
        LinkedList<String> list = randomStr.getIfPresent(key);
        if (list == null) {
            list = new LinkedList<>();
        }
        Set<String> setData = this.createRandomStr(strLength, maxCount);
        list.addAll(setData);
        randomStr.put(key, list);
    }

    @Override
    public String getRandomStr(String key,int strLength) {
        try {
            if (this.getRandomSize(key) < minCount) {
                this.addRandomStr(key,strLength);
            }
            LinkedList<String> list = randomStr.get(key);
            String result = list.getFirst();
            list.removeFirst();
            randomStr.put(key, list);
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer getRandomSize(String key) {
        LinkedList<String> list = randomStr.getIfPresent(key);
        return list != null ? list.size() : 0;
    }

}
