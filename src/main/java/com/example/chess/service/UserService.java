package com.example.chess.service;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.cache.CacheTemplate;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.catan.CatanPlayer;
import com.example.chess.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

@Component
public class UserService {

    @Autowired
    private CacheTemplate cacheTemplate;

    /**
     * 登录
     */
    public void doLogin(Session session, Result message) {
        //TODO:   获取用户唯一id、
        //        从Redis中获取：用户关联 session、用户所在 roomId
        //        如果未获取到：不做任何操作
        //        如果获取到：则自动进入房间
        //        读取用户缓存信息，roomId、合并 session
        //        读取房间缓存信息。
    }

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
