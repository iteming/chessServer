package com.example.chess.cache.redis;

import com.example.chess.cache.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JedisCacheTemplate implements CacheTemplate {

    private final JedisPool jedisPool;


    public JedisCacheTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void set(String key, String value, int validTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, validTime, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.returnResource(jedis);
        }

    }

    @Override
    public void expire(String key, int validTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, validTime);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.returnResource(jedis);
        }
    }

    @Override
    public Long getExpire(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.returnResource(jedis);
        }
        return null;
    }

    @Override
    public Boolean persist(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long persist = jedis.persist(key);
            if (persist != null) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.returnResource(jedis);
        }
        return false;
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Integer append(String key, String str) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.append(key, str).intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public List<String> getList(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.mget(keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Long len(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.llen(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }


    @Override
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void hSet(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public String hGet(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }


    @Override
    public Map<String, String> hGetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }


    @Override
    public void hDel(String key, List<Object> fields) {
        String[] fieldsArr = (String[]) fields.stream().map(String::valueOf).toArray();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, fieldsArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void hSet(String key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, hash);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public Boolean hasKey(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return false;
    }

    @Override
    public Long hIncrByLong(String key, String field, Long value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Double hIncrByDouble(String key, String field, Double value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hincrByFloat(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Integer hSize(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hlen(key).intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public void lPush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lPush(String key, List<String> values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] valuesArr = (String[]) values.stream().map(String::valueOf).toArray();
            jedis.lpush(key, valuesArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lrPush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lrPush(String key, List<String> values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] valuesArr = (String[]) values.stream().map(String::valueOf).toArray();
            jedis.rpush(key, valuesArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lSet(String key, Long index, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lset(key, index, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lRem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lrem(key, count, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void lTrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.ltrim(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public String lPop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public String lrPop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public String lIndex(String key, Long index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lindex(key, index);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Integer lLen(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.llen(key).intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public void lSet(String key, Integer index, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lset(key, index, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }


    @Override
    public void sAdd(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.sadd(key, values);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void sRem(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.srem(key, values);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public String sPop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.spop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Set<String> sDiff(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sdiff(keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Long sDiffStore(List<String> keys, String storeKey) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sdiffstore(storeKey, keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Set<String> sInter(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sinter(keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Long sInterStore(List<String> keys, String storeKey) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sinterstore(storeKey, keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Set<String> sUnion(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sunion(keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Long sUnionStore(List<String> keys, String storeKey) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String[] keysArr = (String[]) keys.stream().map(String::valueOf).toArray();
            return jedis.sunionstore(storeKey, keysArr);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Integer sSize(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key).intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Boolean sIsMember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public String sRandMember(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srandmember(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    @Override
    public Set<String> sMembersAll(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.smembers(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return null;
    }

    /**
     * 返还到连接池
     *
     * @param jedis
     */
    private void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
