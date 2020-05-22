package com.example.chess.cache;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 江斌  <br/>
 * @version 1.0  <br/>
 * @ClassName: CacheClient <br/>
 * @date: 2019/10/26 16:45 <br/>
 * @Description: 这里写注释 <br/>
 * @since JDK 1.8
 */
@Component
public interface CacheTemplate {

    int DEFAULT_VALID_TIME = 60 * 60 * 1000;

    /**
     * 存取缓存
     *
     * @param key
     * @param value
     * @param validTime 秒
     */
    void set(String key, String value, int validTime);

    /**
     * 设置有效期
     *
     * @param key
     * @param validTime 秒
     */
    void expire(String key, int validTime);

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间
     *
     * @param key
     * @return
     */
    Long getExpire(String key);


    /**
     * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )
     *
     * @param key
     * @return
     */
    Boolean persist(String key);

    /**
     * 获取缓存对象 字符串
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    Integer append(String key, String str);

    /**
     * 通过批量的key获取批量的value
     *
     * @param keys
     * @return
     */
    List<String> getList(List<String> keys);


    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    Long len(String key);

    /**
     * 删除缓存
     *
     * @param key
     */
    void del(String key);

    /**
     * hash
     *
     * @param key
     * @param field
     * @return
     */
    String hGet(String key, String field);


    /**
     * @param key
     * @return
     */
    Map<String, String> hGetAll(String key);


    /**
     * @param key
     * @param field
     * @param value
     */
    void hSet(String key, String field, String value);

    /**
     * @param key
     * @param fields
     */
    void hDel(String key, List<Object> fields);


    /**
     * @param key
     * @param hash
     */
    void hSet(String key, Map<String, String> hash);

    /**
     * 检查是否存在
     *
     * @param key
     * @param field
     * @return
     */
    Boolean hasKey(String key, String field);

    /**
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hIncrByLong(String key, String field, Long value);


    /**
     * @param key
     * @param field
     * @param value
     * @return
     */
    Double hIncrByDouble(String key, String field, Double value);

    /**
     * @param key
     * @return
     */
    Integer hSize(String key);

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param value
     * @return
     */
    void lPush(String key, String value);

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param values
     * @return
     */
    void lPush(String key, List<String> values);

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param value
     * @return
     */
    void lrPush(String key, String value);

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param values
     * @return
     */
    void lrPush(String key, List<String> values);


    /**
     * 通过key设置list指定下标位置的value
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    void lSet(String key, Long index, String value);

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    void lRem(String key, long count, String value);

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    void lTrim(String key, long start, long end);

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    String lPop(String key);


    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    String lrPop(String key);


    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * @return
     */
    String lIndex(String key, Long index);


    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    Integer lLen(String key);

    /**
     * 通过key获取list指定下标位置的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> lRange(String key, long start, long end);


    /**
     * 将列表 key 下标为 index 的元素的值设置为 value
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    void lSet(String key, Integer index, String value);

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param values
     * @return
     */
    void sAdd(String key, String... values);

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param values
     * @return
     */
    void sRem(String key, String... values);

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    String sPop(String key);

    /**
     * 通过key获取set中的差集
     *
     * @param keys
     * @return
     */
    Set<String> sDiff(List<String> keys);

    /**
     * 通过key获取set中的差集并存入到另一个key中
     *
     * @param keys
     * @param storeKey
     * @return
     */
    Long sDiffStore(List<String> keys, String storeKey);

    /**
     * 通过key获取指定set中的交集
     *
     * @param keys
     * @return
     */
    Set<String> sInter(List<String> keys);


    /**
     * 通过key获取指定set中的交集 并将结果存入新的set中
     *
     * @param keys
     * @param storeKey
     * @return
     */
    Long sInterStore(List<String> keys, String storeKey);


    /**
     * 通过key返回所有set的并集
     *
     * @param keys
     * @return
     */
    Set<String> sUnion(List<String> keys);

    /**
     * 通过key返回所有set的并集,并存入到新的set中
     *
     * @param keys
     * @param storeKey
     * @return
     */
    Long sUnionStore(List<String> keys, String storeKey);


    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    Integer sSize(String key);

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    Boolean sIsMember(String key, String member);


    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    String sRandMember(String key);

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    Set<String> sMembersAll(String key);

}
