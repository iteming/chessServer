package com.example.chess.cache;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Component
public interface CacheNotice {

   void onRemovalStr(String key, CacheEntity<String> cacheEntity);

    CacheEntity<String> loadStr(String key);


    void onRemovalHash(String key, CacheEntity<ConcurrentHashMap<String, String>> cacheEntity);

    CacheEntity<ConcurrentHashMap<String, String>> loadHash(String key);

    void onRemovalList(String key, CacheEntity<LinkedList<String>> cacheEntity);

    CacheEntity<LinkedList<String>> loadList(String key);

    void onRemovalSet(String key, CacheEntity<LinkedHashSet<String>> cacheEntity);

    CacheEntity<LinkedHashSet<String>> loadSet(String key);

}
