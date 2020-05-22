package com.example.chess.service;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.cache.CacheTemplate;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.catan.CatanPlayer;
import com.example.chess.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private CacheTemplate cacheTemplate;


    public CatanPlayer getUser(String token) {
        String userString = cacheTemplate.get(token);
        if (StringUtils.isBlank(userString)){
            throw new BizException("获取用户信息为空");
        }
        return JSONObject.parseObject(userString, CatanPlayer.class);
    }

    public Result setUser(String token, String user) {
        cacheTemplate.set(token, user, CacheTemplate.DEFAULT_VALID_TIME);
        return Result.success();
    }
}
