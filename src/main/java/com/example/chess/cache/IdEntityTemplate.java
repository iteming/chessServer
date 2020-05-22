package com.example.chess.cache;


import com.example.chess.utils.RandomUtil;

import java.util.Set;

public interface IdEntityTemplate {

    Long DEFAULT_INIT = 10000L;
    /**
     * 自增的ID的key
     */
    String AUTO_ID_KEY = "AUTO_ID";

    /**
     * 自增的编码
     */
    String AUTO_CODE_KEY = "AUTO_CODE";



    String RANDOM_STR = "RANDOM_STR";


    /**
     * 哈希自增
     *
     * @param key
     * @param field
     * @param initialVal
     * @return
     */
    Long getAutoLong(String key, String field, Long initialVal);


    /**
     * @param key
     * @param field
     * @return
     */
    Boolean exists(String key, String field);

    /**
     * 创建值
     *
     * @param key
     * @param field
     * @param initialVal
     * @return
     */
    Long createAutoLong(String key, String field, Long initialVal);


    /**
     * 创建随机字符串
     *
     * @param strLength
     * @param maxCount
     * @return
     */
    default Set<String> createRandomStr(int strLength, int maxCount) {
        return RandomUtil.createSetData(strLength, maxCount);
    }

    /**
     * 添加随机字符
     *
     * @param key
     */
    void addRandomStr(String key, int strLength);

    /**
     * @param key
     * @return
     */
    String getRandomStr(String key, int strLength);

    /**
     * @param key
     * @return
     */
    Integer getRandomSize(String key);

}
