package com.example.chess.cache;

public class CacheUtil {


    public static String getKey(String appName, String majorKey) {
        String buffer = appName + ":" + majorKey;
        return buffer.toUpperCase();
    }

    public static String getKey(String appName, String majorKey, String tableName) {
        String buffer = appName + ":" + majorKey + "_" + tableName;
        return buffer.toUpperCase();
    }

//    public static Long getAutoId(String tableName, String columnName) {
//        return getAutoId(tableName, columnName, DEFAULT_INIT);
//    }

//    public static Long getAutoId(String tableName, String columnName, Long initialVal) {
//        IdEntityTemplate idEntityTemplate = SpringContextUtil.getBean(IdEntityTemplate.class);
//        String key = CacheUtil.getKey(SystemInfoUtil.getApplication(), AUTO_ID_KEY);
//        String field = (tableName + "_" + columnName).toUpperCase();
//        return idEntityTemplate.getAutoLong(key, field, initialVal);
//    }

//    public static Long getAutoCode(String code, String field, Long initialVal) {
//        IdEntityTemplate idEntityTemplate = SpringContextUtil.getBean(IdEntityTemplate.class);
//        String key = CacheUtil.getKey(SystemInfoUtil.getApplication(), AUTO_CODE_KEY, code);
//        return idEntityTemplate.getAutoLong(key, field, initialVal);
//    }

//    public static String getAutoDayCode(String code,int strLength) {
//        IdEntityTemplate idEntityTemplate = SpringContextUtil.getBean(IdEntityTemplate.class);
//        String dateStr = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
//        String key = CacheUtil.getKey(SystemInfoUtil.getApplication(), RANDOM_STR, code);
//        String randomStr = idEntityTemplate.getRandomStr(key,strLength);
//        return dateStr.concat(String.valueOf(randomStr));
//    }

//    public static String getRandomStr(String code,int strLength) {
//        IdEntityTemplate idEntityTemplate = SpringContextUtil.getBean(IdEntityTemplate.class);
//        String key = CacheUtil.getKey(SystemInfoUtil.getApplication(), RANDOM_STR, code);
//        return idEntityTemplate.getRandomStr(key,strLength);
//    }
}
