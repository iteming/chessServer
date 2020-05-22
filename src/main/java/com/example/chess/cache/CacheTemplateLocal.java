package com.example.chess.cache;

import com.example.chess.exception.BizException;
import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CacheTemplateLocal implements CacheTemplate {


    private static LoadingCache<String, CacheEntity<String>> strLocalCache;
    private static LoadingCache<String, CacheEntity<ConcurrentHashMap<String, String>>> hashLocalCache;
    private static LoadingCache<String, CacheEntity<LinkedList<String>>> listLocalCache;
    private static LoadingCache<String, CacheEntity<LinkedHashSet<String>>> setLocalCache;

    private final static int concurrencyLevel = 10;

    private final static long duration = 60 * 60;

    private final static int initialCapacity = 10;

    private final static int size = 1000;

    public CacheTemplateLocal(CacheNotice cacheNotice) {
        strLocalCache = CacheBuilder.newBuilder()
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
                .removalListener(new RemovalListener<String, CacheEntity<String>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, CacheEntity<String>> removalNotification) {
                        cacheNotice.onRemovalStr(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, CacheEntity<String>>() {
                    @Override
                    public CacheEntity<String> load(String key) throws Exception {
                        return cacheNotice.loadStr(key);
                    }
                });

        hashLocalCache = CacheBuilder.newBuilder()
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
                .removalListener(new RemovalListener<String, CacheEntity<ConcurrentHashMap<String, String>>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, CacheEntity<ConcurrentHashMap<String, String>>> removalNotification) {
                        cacheNotice.onRemovalHash(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, CacheEntity<ConcurrentHashMap<String, String>>>() {
                    @Override
                    public CacheEntity<ConcurrentHashMap<String, String>> load(String key) throws Exception {
                        return cacheNotice.loadHash(key);
                    }
                });

        listLocalCache = CacheBuilder.newBuilder()
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
                .removalListener(new RemovalListener<String, CacheEntity<LinkedList<String>>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, CacheEntity<LinkedList<String>>> removalNotification) {
                        cacheNotice.onRemovalList(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, CacheEntity<LinkedList<String>>>() {
                    @Override
                    public CacheEntity<LinkedList<String>> load(String key) throws Exception {
                        return cacheNotice.loadList(key);
                    }
                });

        setLocalCache = CacheBuilder.newBuilder()
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
                .removalListener(new RemovalListener<String, CacheEntity<LinkedHashSet<String>>>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, CacheEntity<LinkedHashSet<String>>> removalNotification) {
                        cacheNotice.onRemovalSet(removalNotification.getKey(), removalNotification.getValue());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, CacheEntity<LinkedHashSet<String>>>() {
                    @Override
                    public CacheEntity<LinkedHashSet<String>> load(String key) throws Exception {
                        return cacheNotice.loadSet(key);
                    }
                });
    }

    @Override
    public void set(String key, String value, int validTime) {
        strLocalCache.put(key, new CacheEntity<>(key, value, DEFAULT_VALID_TIME));
    }

    @Override
    public void expire(String key, int validTime) {
        throw new BizException("暂不支持该方法【expire】");
    }

    @Override
    public Long getExpire(String key) {
        throw new BizException("暂不支持该方法【getExpire】");
    }

    @Override
    public Boolean persist(String key) {
        throw new BizException("暂不支持该方法【persist】");
    }

    @Override
    public String get(String key) {
        try {
            return strLocalCache.get(key).getValue();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer append(String key, String str) {
        throw new BizException("暂不支持该方法【append】");
    }

    @Override
    public List<String> getList(List<String> keys) {
        ImmutableMap<String, CacheEntity<String>> allPresent = strLocalCache.getAllPresent(keys);
        if (allPresent.isEmpty()) {
            return null;
        }
        return allPresent.values().stream().map(CacheEntity::getValue).collect(Collectors.toList());
    }

    @Override
    public Long len(String key) {
        return strLocalCache.size();
    }

    @Override
    public void del(String key) {
        strLocalCache.invalidate(key);
    }

    @Override
    public String hGet(String key, String field) {
        try {
            CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.get(key);
            if (cacheEntity != null) {
                return cacheEntity.getValue().get(field);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        try {

            CacheEntity<ConcurrentHashMap<String, String>> mapCacheEntity = hashLocalCache.get(key);
            if (mapCacheEntity != null) {
                return mapCacheEntity.getValue();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void hSet(String key, String field, String value) {

        CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
            cacheEntity = new CacheEntity<>(key, map, DEFAULT_VALID_TIME, CacheEntity.DataType.Hash);
        }
        cacheEntity.getValue().put(key, value);
        hashLocalCache.put(key, cacheEntity);


    }

    @Override
    public void hDel(String key, List<Object> fields) {

        CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.getIfPresent(key);
        ConcurrentHashMap<String, String> map;
        if (cacheEntity != null) {
            map = new ConcurrentHashMap<>(cacheEntity.getValue());
            fields.forEach(map::remove);
            if (map.size() != 0) {
                cacheEntity.setValue(map);
                hashLocalCache.put(key, cacheEntity);
            } else {
                hashLocalCache.invalidate(key);
            }
        }

    }

    @Override
    public void hSet(String key, Map<String, String> hash) {

        CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.getIfPresent(key);
        ConcurrentHashMap<String, String> map;
        if (cacheEntity != null) {
            map = new ConcurrentHashMap<>(cacheEntity.getValue());
            map.putAll(hash);
            cacheEntity.setValue(map);
            hashLocalCache.put(key, cacheEntity);
        }

    }

    @Override
    public Boolean hasKey(String key, String field) {
        CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return false;
        } else {
            HashMap<String, String> map = new HashMap<>(cacheEntity.getValue());
            return map.get(field) != null;
        }
    }

    @Override
    public Long hIncrByLong(String key, String field, Long value) {
        throw new BizException("暂不支持该方法【hIncrByLong】");
    }

    @Override
    public Double hIncrByDouble(String key, String field, Double value) {
        throw new BizException("暂不支持该方法【hIncrByDouble】");
    }

    @Override
    public Integer hSize(String key) {
        CacheEntity<ConcurrentHashMap<String, String>> cacheEntity = hashLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        } else {
            return cacheEntity.getValue().size();
        }
    }

    @Override
    public void lPush(String key, String value) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            LinkedList<String> list = new LinkedList<>();
            cacheEntity = new CacheEntity<>(key, list, DEFAULT_VALID_TIME, CacheEntity.DataType.List);
        }
        cacheEntity.getValue().addFirst(value);
        listLocalCache.put(key, cacheEntity);
    }

    @Override
    public void lPush(String key, List<String> values) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            LinkedList<String> list = new LinkedList<>();
            cacheEntity = new CacheEntity<>(key, list, DEFAULT_VALID_TIME, CacheEntity.DataType.List);
        }
        cacheEntity.getValue().addAll(0, values);
        listLocalCache.put(key, cacheEntity);
    }

    @Override
    public void lrPush(String key, String value) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            LinkedList<String> list = new LinkedList<>();
            cacheEntity = new CacheEntity<>(key, list, DEFAULT_VALID_TIME, CacheEntity.DataType.List);
        }
        cacheEntity.getValue().addLast(value);
        listLocalCache.put(key, cacheEntity);

    }

    @Override
    public void lrPush(String key, List<String> values) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            LinkedList<String> list = new LinkedList<>();
            cacheEntity = new CacheEntity<>(key, list, DEFAULT_VALID_TIME, CacheEntity.DataType.List);
        }
        cacheEntity.getValue().addAll(values);
        listLocalCache.put(key, cacheEntity);
    }

    @Override
    public void lSet(String key, Long index, String value) {
        throw new BizException("暂不支持该方法【lRem】");
    }

    @Override
    public void lRem(String key, long count, String value) {
        throw new BizException("暂不支持该方法【lRem】");
    }

    @Override
    public void lTrim(String key, long start, long end) {
        throw new BizException("暂不支持该方法【lTrim】");
    }

    @Override
    public String lPop(String key) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        String first = cacheEntity.getValue().removeFirst();
        if (cacheEntity.getValue().isEmpty()) {
            listLocalCache.invalidate(key);
        } else {
            listLocalCache.put(key, cacheEntity);
        }
        return first;
    }

    @Override
    public String lrPop(String key) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        String last = cacheEntity.getValue().removeLast();

        if (cacheEntity.getValue().isEmpty()) {
            listLocalCache.invalidate(key);
        } else {
            listLocalCache.put(key, cacheEntity);
        }
        return last;
    }

    @Override
    public String lIndex(String key, Long index) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        String value = cacheEntity.getValue().get(index.intValue());
        listLocalCache.put(key, cacheEntity);
        return value;
    }

    @Override
    public Integer lLen(String key) {
        CacheEntity<LinkedList<String>> cacheEntity = listLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        return cacheEntity.getValue().size();
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        throw new BizException("暂不支持该方法【lRange】");
    }

    @Override
    public void lSet(String key, Integer index, String value) {
        throw new BizException("暂不支持该方法【lSet】");
    }

    @Override
    public void sAdd(String key, String... values) {
        CacheEntity<LinkedHashSet<String>> cacheEntity = setLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            LinkedHashSet<String> hashSet = new LinkedHashSet<>();
            cacheEntity = new CacheEntity<>(key, hashSet, DEFAULT_VALID_TIME, CacheEntity.DataType.Set);
        }
        cacheEntity.getValue().addAll(Arrays.asList(values));
        setLocalCache.put(key, cacheEntity);
    }

    @Override
    public void sRem(String key, String... values) {
        CacheEntity<LinkedHashSet<String>> cacheEntity = setLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return;
        }
        cacheEntity.getValue().removeAll(Arrays.asList(values));

        if (cacheEntity.getValue().isEmpty()) {
            setLocalCache.invalidate(key);
        } else {
            setLocalCache.put(key, cacheEntity);
        }
    }

    @Override
    public String sPop(String key) {
        throw new BizException("暂不支持该方法【sPop】");
    }

    @Override
    public Set<String> sDiff(List<String> keys) {
        throw new BizException("暂不支持该方法【sDiff】");
    }

    @Override
    public Long sDiffStore(List<String> keys, String storeKey) {
        throw new BizException("暂不支持该方法【sDiffStore】");
    }

    @Override
    public Set<String> sInter(List<String> keys) {
        throw new BizException("暂不支持该方法【sInter】");
    }

    @Override
    public Long sInterStore(List<String> keys, String storeKey) {
        throw new BizException("暂不支持该方法【sInterStore】");
    }

    @Override
    public Set<String> sUnion(List<String> keys) {
        throw new BizException("暂不支持该方法【sUnion】");
    }

    @Override
    public Long sUnionStore(List<String> keys, String storeKey) {
        throw new BizException("暂不支持该方法【sUnionStore】");
    }

    @Override
    public Integer sSize(String key) {
        CacheEntity<LinkedHashSet<String>> cacheEntity = setLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        return cacheEntity.getValue().size();
    }

    @Override
    public Boolean sIsMember(String key, String member) {
        CacheEntity<LinkedHashSet<String>> cacheEntity = setLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return false;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return false;
        }
        return cacheEntity.getValue().contains(member);
    }

    @Override
    public String sRandMember(String key) {
        throw new BizException("暂不支持该方法【sRandMember】");
    }

    @Override
    public Set<String> sMembersAll(String key) {
        CacheEntity<LinkedHashSet<String>> cacheEntity = setLocalCache.getIfPresent(key);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.getValue().isEmpty()) {
            return null;
        }
        return cacheEntity.getValue();
    }


}
