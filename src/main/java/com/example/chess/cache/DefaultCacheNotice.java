package com.example.chess.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultCacheNotice implements CacheNotice {

    @Override
    public void onRemovalStr(String key, CacheEntity<String> cacheEntity) {
        log.debug("-----onRemovalStr------{}-----------", key);
    }

    @Override
    public CacheEntity<String> loadStr(String key) {
        log.debug("-----loadStr------{}-----------", key);
        return new CacheEntity<String>(key,null,0);
    }

    @Override
    public void onRemovalHash(String key, CacheEntity<ConcurrentHashMap<String, String>> cacheEntity) {
        log.debug("-----onRemovalHash------{}-----------", key);
    }

    @Override
    public CacheEntity<ConcurrentHashMap<String, String>> loadHash(String key) {
        log.debug("-----loadHash------{}-----------", key);
        return new CacheEntity<ConcurrentHashMap<String, String>>(key,null,0);
    }

    @Override
    public void onRemovalList(String key, CacheEntity<LinkedList<String>> cacheEntity) {
        log.debug("-----onRemovalList------{}-----------", key);
    }

    @Override
    public CacheEntity<LinkedList<String>> loadList(String key) {
        log.debug("-----loadList------{}-----------", key);
        return new CacheEntity<LinkedList<String>>(key,null,0);
    }

    @Override
    public void onRemovalSet(String key, CacheEntity<LinkedHashSet<String>> cacheEntity) {
        log.debug("-----onRemovalSet------{}-----------", key);
    }

    @Override
    public CacheEntity<LinkedHashSet<String>> loadSet(String key) {
        log.debug("-----loadSet------{}-----------", key);
        return new CacheEntity<LinkedHashSet<String>>(key,null,0);
    }
}
