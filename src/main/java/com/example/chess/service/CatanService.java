package com.example.chess.service;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.catan.CatanPlayer;
import com.example.chess.entity.catan.CatanRoom;
import com.example.chess.entity.play.UserContext;
import com.example.chess.protocol.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CatanService {

    private static Integer total = 3;

    /**
     * 匹配房间
     */
    public Room matchRoom(ConcurrentHashMap<String, Room> roomMap) {
        // 匹配没有满人的房间
        for (Map.Entry<String, Room> entry : roomMap.entrySet()) {
            if (!entry.getValue().isFull()) {
                return entry.getValue();
            }
        }
        // 如果全都满了，则再重新创建一个房间
        return createRoom(null, roomMap);
    }

    /**
     * 创建房间
     */
    public Room createRoom(String roomId, ConcurrentHashMap<String, Room> roomMap) {
        if (roomId == null) {
            roomId = "RM" + String.format("%04d", roomMap.size() + 1);
        }
        Room room = new CatanRoom(roomId, total);
        roomMap.put(roomId, room);
        return room;
    }


    public String getUserFromRoom(Session session, ConcurrentHashMap<String, Room> roomMap) {
        Room room = getRoom(session, roomMap);
        if (room == null) return null;
        UserContext<CatanPlayer> userContext = (UserContext<CatanPlayer>) room.getSessions().get(session);
        if (userContext == null) return null;
        return JSONObject.toJSONString(userContext.getPlayer());
    }

    /** ----------------------------------------------------------------------------------- **/

    /**
     * 处理ready事件
     */
    public void doReady(Session session, String message, ConcurrentHashMap<String, Room> roomMap) throws IOException, InterruptedException {
        Room room = getRoom(session, roomMap);
        if (room == null) return;
        room.doReady(session);
    }

    public void doOver(Session session, String message, ConcurrentHashMap<String, Room> roomMap) throws IOException, InterruptedException {
        Room room = getRoom(session, roomMap);
        if (room == null) return;
        room.doOver(session);
    }

    public void leaveRoom(Session session, String message, ConcurrentHashMap<String, Room> roomMap) throws IOException, InterruptedException {
        Room room = getRoom(session, roomMap);
        if (room == null) return;
        room.leaveRoom(session);
    }

    public String getRoomId(Session session) {
        return (String) session.getUserProperties().get("roomId");
    }

    private Room getRoom(Session session, ConcurrentHashMap<String, Room> roomMap) {
        String roomId = getRoomId(session);
        if (roomId == null) return null;
        return roomMap.get(roomId);
    }


    /** ----------------------------------------------------------------------------------- **/

    public String startAction(Session session, String message, ConcurrentHashMap<String, Room> roomMap) {
        Room room = getRoom(session, roomMap);
        Result result = JSONObject.parseObject(message, Result.class);
        ActionTypeEnum actionTypeEnum = ActionTypeEnum.getEnum(result.getStatus());
        room.startAction(actionTypeEnum);

        return (String) session.getUserProperties().get("roomId");
    }
}
