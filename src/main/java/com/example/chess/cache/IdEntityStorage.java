package com.example.chess.cache;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface IdEntityStorage {

    Map<String, AtomicLong> loadAutoCode(String key);

    void onRemovalAutoCode(String key, Map<String, AtomicLong> map);

    LinkedList<String> loadRandomStr(String key);

    void onRemovalRandomStr(String key, LinkedList<String> linkedList);
}
