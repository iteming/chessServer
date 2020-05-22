package com.example.chess.cache;

public interface LockTemplate {

    String LOCK_SUCCESS = "OK";
    String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    String LOCK_EKY = "LOCK_EKY";

    default String getLockKey(String key) {
        return CacheUtil.getKey(LOCK_EKY, key);
    }

    /**
     * 获取锁。在 timeout 时间内如果获取失败失败，会重试
     *
     * @param lockKey   锁key
     * @param requestId key 对应的 值，用来防止多个客户端获取到同一个锁。可以用UUID来产生
     * @param expire    锁超时时间。防止由于异常情况下，锁一直未被释放
     * @param timeout   获取锁超时时间，超时时间内，可以重试
     * @return 锁获取成功，返回true；否则，返回false
     */
    default boolean tryLock(String lockKey, String requestId, int expire, int timeout) throws InterruptedException {
        Long endTime = System.currentTimeMillis() + timeout * 1000L;
        while (endTime >= System.currentTimeMillis()) {
            boolean lock = this.lock(lockKey, requestId, expire);
            if (lock) {
                return true;
            }
            Thread.sleep(100L);
        }
        return false;
    }

    /**
     * 尝试获取锁。无论获取成功还是失败，直接返回（获取失败的情况下不会重试）
     *
     * @param lockKey   锁key
     * @param requestId key 对应的 值，用来防止多个客户端获取到同一个锁。可以用UUID来产生
     * @param expire    锁超时时间。防止由于异常情况下，锁一直未被释放
     * @return 锁获取成功，返回true；否则，返回false
     */
    boolean lock(String lockKey, String requestId, int expire);

    /**
     * 释放锁
     *
     * @param lockKey   锁key
     * @param requestId key 对应的 值，用来防止多个客户端获取到同一个锁。可以用UUID来产生
     */
    void unlock(String lockKey, String requestId);

}
