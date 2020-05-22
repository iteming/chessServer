package com.example.chess.service;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.cache.CacheTemplate;
import com.example.chess.cache.CacheTemplateLocal;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.catan.CatanPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private CacheTemplate cacheTemplate;


    public CatanPlayer getUser(String token) {
        String userString = cacheTemplate.get(token);
        return JSONObject.parseObject(userString, CatanPlayer.class);
//        return null;
    }

    public Result setUser(String token, String user) {
        cacheTemplate.set(token, user, 60 * 60 * 12);
        return Result.success();
//        return null;
    }
}
