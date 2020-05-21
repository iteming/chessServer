package com.example.chess.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.RunContext;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.catan.CatanRoom;
import com.example.chess.protocol.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/catan")
@Component
@Slf4j
public class WebsocketCatan {

    private static Integer total = 3;
    private static ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

    // 静态变量，用来记录当前在线链接数。应该把他设计成线程安全的。
    private static int onlineCount = 0;

    // 客户端发送数据对象
    private Session session;

    static {
        System.out.println("Deamon Thread Created!");
        log.debug("Deamon Thread Created! ----- ");
        RunContext context = new RunContext(roomMap);
        DeamonThread deamonThread = new DeamonThread(context);
        Thread dThread = new Thread(deamonThread);
        dThread.start();
    }

    /**
     * 连接成功时调用的方法
     *
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        // 在线数加1
        addOnlineCount();

        log.debug("有新加入链接！当前在线人数为： --" + getOnlineCount() + "--");
        log.debug("[" + this.session.getId() + "] Client connected");
    }

    /**
     * 连接关闭调用的方法
     *
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        // 在线人数减1
        subOnlineCount();

        log.debug("有一连接关闭！当前在线人数为： --" + getOnlineCount() + "--");
        log.debug("[" + this.session.getId() + "] Connection closed");
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误" + error.getMessage());
        error.printStackTrace();
    }

    // 计数相关
    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebsocketCatan.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebsocketCatan.onlineCount--;
    }


    /** --------------------------------------------------------------------------------------------- **/

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        // 收到前端发来的消息，进行解析
        Result result = JSONObject.parseObject(message, Result.class);
        ActionTypeEnum resultEnum = ActionTypeEnum.getEnum(result.getStatus());
        switch (resultEnum) {
            case LOGIN: // 登录，缓存持久化
                doLogin(session, result);
                break;

            case CONNECT_START: // 进入房间，后续的观战
                doConnect(session, result);
                break;

            case MATCH_START: // 开始匹配
                if (doConnect(session, result)) {
                    // TODO: 向Redis存入用户信息：Map<userId, userInfo> = 用户id , roomId + session
                    // 进入房间后，如果房间人数已满，则所有人都准备
                    doReady(session, message);
                }
                break;
            case MATCH_CANCEL: // 取消匹配
                leaveRoom(session, message);
                break;


            default:
                break;
        }


        // 控制台打印前端发送过来的消息
        log.debug("控制台打印前端发送过来的消息" + message);
    }


    /**
     * 处理ready事件
     */
    private void doReady(Session session, String message) throws IOException, InterruptedException {
        Room room = getRoom(session);
        room.doReady(session);
    }

    private void doOver(Session session, String message) throws IOException, InterruptedException {
        Room room = getRoom(session);
        room.doOver(session);
    }

    private void leaveRoom(Session session, String message) throws IOException, InterruptedException {
        Room room = getRoom(session);
        room.leaveRoom(session);
    }

    private String getRoomId(Session session) {
        return (String) session.getUserProperties().get("roomId");
    }

    private Room getRoom(Session session) {
        return roomMap.get(getRoomId(session));
    }

    /**
     * 登录
     */
    private void doLogin(Session session, Result message) {
        //TODO:   获取用户唯一id、
        //        从Redis中获取：用户关联 session、用户所在 roomId
        //        如果未获取到：不做任何操作
        //        如果获取到：则自动进入房间
        //        读取用户缓存信息，roomId、合并 session
        //        读取房间缓存信息。
    }

    /**
     * 处理CONNECT请求
     */
    private boolean doConnect(Session session, Result message) throws IOException, InterruptedException {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        log.info("A new Client Connect and the roomid is [" + roomId + "]");

        Room room;
        // 如果没有输入 roomId ，则自动匹配 room
        if (roomId == null) {
            room = matchRoom();
        } else {
            room = roomMap.get(roomId);
        }

        // 进入房间
        if (room.enterRoom(session)) {
            session.getUserProperties().put("roomId", roomId);
            return true;
        } else {
            log.debug("---- 进入房间失败 ----");
            Result result = Result.failed(new Result(ActionTypeEnum.CONNECT_START.getCode(), ActionTypeEnum.CONNECT_START.getMessage()));
            session.getBasicRemote().sendText(JSONObject.toJSONString(result));
            return false;
        }
    }

    /**
     * 匹配房间
     */
    private Room matchRoom() {
        // 匹配没有满人的房间
        for (Map.Entry<String, Room> entry : roomMap.entrySet()) {
            if (!entry.getValue().isFull()) {
                return entry.getValue();
            }
        }
        // 如果全都满了，则再重新创建一个房间
        return createRoom();
    }

    /**
     * 创建房间
     */
    private Room createRoom() {
        String roomId = "RM" + String.format("%04d", roomMap.size() + 1);
        Room room = new CatanRoom(roomId, total);
        roomMap.put(roomId, room);
        return room;
    }
}
