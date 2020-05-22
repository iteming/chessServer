package com.example.chess.cache;

import com.example.chess.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class DefaultIdEntityStorage implements IdEntityStorage {

   private final int strLength;
   private final int maxCount;

    public DefaultIdEntityStorage(int strLength, int maxCount) {
        this.strLength = strLength;
        this.maxCount = maxCount;
    }

    @Override
    public Map<String, AtomicLong> loadAutoCode(String key) {
        log.debug("-----loadAutoCode------{}-----------", key);
        return new HashMap<>();
    }

    @Override
    public void onRemovalAutoCode(String key, Map<String, AtomicLong> map) {
        log.debug("-----onRemovalAutoCode---key----{}-----------", key);
        log.debug("-----onRemovalAutoCode---data---{}-----------", map.toString());
    }

    @Override
    public LinkedList<String> loadRandomStr(String key) {
        log.debug("-----loadRandomStr------{}-----------", key);
        Set<String> setData = RandomUtil.createSetData(strLength, maxCount);
        return new LinkedList<>(setData);
    }

    @Override
    public void onRemovalRandomStr(String key, LinkedList<String> linkedList) {
        log.debug("-----onRemovalRandomStr---key----{}-----------", key);
        log.debug("-----onRemovalRandomStr---data---{}-----------", linkedList.toString());
    }
}
