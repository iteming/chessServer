package com.example.chess.cache.redis;

import com.example.chess.cache.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author : jiangbin
 * @Date : 2020/2/24 09:23
 * @Description :
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class JedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPool jedisPoolFactory() {
        System.out.println("JedisPool注入开始...");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        // 连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(true);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,
                redisProperties.getHost(),
                redisProperties.getPort(),
                Math.toIntExact(redisProperties.getTimeout().toMillis()),
                redisProperties.getPassword(),
                redisProperties.getDatabase());
        System.out.println("JedisPool注入成功...");
        return jedisPool;
    }

//    @Primary
//    @Bean
//    public LockTemplate lockTemplate() {
//        return new JedisLockTemplate(jedisPoolFactory());
//    }

    @Primary
    @Bean
    public CacheTemplate cacheTemplate() {
        return new JedisCacheTemplate(jedisPoolFactory());
    }
}
