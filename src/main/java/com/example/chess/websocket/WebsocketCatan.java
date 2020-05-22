package com.example.chess.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.RunContext;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.catan.CatanPlayer;
import com.example.chess.protocol.ActionTypeEnum;
import com.example.chess.service.CatanService;
import com.example.chess.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/catan/{token}")
@Component
@Slf4j
public class WebsocketCatan {

    private static ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

    // 静态变量，用来记录当前在线链接数。应该把他设计成线程安全的。
    private static int onlineCount = 0;
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

    // 客户端发送数据对象
    private Session session;
    private String token;
    private String roomId;

    static {
        System.out.println("Deamon Thread Created!");
        log.debug("Deamon Thread Created! ----- ");
        RunContext context = new RunContext(roomMap);
        DeamonThread deamonThread = new DeamonThread(context);
        Thread dThread = new Thread(deamonThread);
        dThread.start();
    }

    private UserService userService;
    private CatanService catanService;
    @Autowired
    public WebsocketCatan(UserService userService, CatanService catanService) {
        System.out.println("构造函数 Created!");
        this.userService = userService;
        this.catanService = catanService;
    }

    /**
     * 连接成功时调用的方法
     *
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam(value = "token") String token, Session session) throws IOException {
        this.session = session;
        // 在线数加1
        addOnlineCount();

        CatanPlayer authAccount = userService.getUser(token);
        if (authAccount == null) {
            authAccount = new CatanPlayer();
            authAccount.setId(token);
        }

        // 获取用户缓存
        this.token = authAccount.getId();
        // 获取用户所在房间id
        this.roomId = authAccount.getRoomId();

        // 断线重连
        if (this.roomId != null) Disconnected(session);

        // 设置用户信息
        userService.setUser(this.token, JSONObject.toJSONString(authAccount));

        log.debug("有新加入链接！当前在线人数为： --" + getOnlineCount() + "--");
        log.debug("[" + this.session.getId() + "] Client connected");
    }

    /**
     * 连接关闭调用的方法
     *
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException, InterruptedException {
        // 在线人数减1
        subOnlineCount();

        catanService.leaveRoom(session, "", roomMap);

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
    public void onError(Session session, Throwable error) throws IOException, InterruptedException {
        catanService.leaveRoom(session, "", roomMap);

        log.error("发生错误" + error.getMessage());
        error.printStackTrace();
    }

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
                userService.doLogin(session, result);
                break;

            case CONNECT_START: // 进入房间，后续的观战
                doConnect(session, result);
                break;

            case MATCH_START: // 开始匹配
                if (doConnect(session, result)) {
                    // TODO: 向Redis存入用户信息：Map<userId, userInfo> = 用户id , roomId + session
                    // 进入房间后，如果房间人数已满，则所有人都准备
                    catanService.doReady(session, message, roomMap);
                }
                break;
            case MATCH_CANCEL: // 取消匹配
                catanService.leaveRoom(session, message, roomMap);
                break;


            default:
                break;
        }

        // 设置用户信息
        userService.setUser(this.token, catanService.getUserFromRoom(session, roomMap));

        // 控制台打印前端发送过来的消息
        log.debug("控制台打印前端发送过来的消息" + message);
    }


    /**
     * 处理CONNECT请求
     */
    private boolean doConnect(Session session, Result message) throws IOException, InterruptedException {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList != null ? roomList.get(0) : null;
        log.info("A new Client Connect and the roomid is [" + roomId + "]");

        Room room;
        // 如果没有输入 roomId ，则自动匹配 room
        if (roomId == null || roomId.equals("")) {
            room = catanService.matchRoom(roomMap);
        } else {
            room = roomMap.get(roomId);
            if (room == null) {
                room = catanService.createRoom(roomId, roomMap);
            }
        }
        return enterRoom(session, room);
    }

    /**
     * 断线重连
     */
    private void Disconnected(Session session) throws IOException {
        Room room = roomMap.get(roomId);
        // 如果房间状态正在运行中，则自动进入房间
        if (room != null && room.getGameStatus() == Room.GAME_STATUS.RUNNING) {
            enterRoom(session, room);
        }
    }

    /**
     * 进入房间
     */
    private boolean enterRoom(Session session, Room room) throws IOException {
        CatanPlayer player = userService.getUser(this.token);
        assert player != null;
        player.setRoomId(room.getRoomId());
        // 进入房间
        if (room.enterRoom(session, player)) {
            session.getUserProperties().put("roomId", room.getRoomId());
            return true;
        } else {
            log.debug("---- 进入房间失败 ----");
            Result result = Result.failed(new Result(ActionTypeEnum.CONNECT_START.getCode(), ActionTypeEnum.CONNECT_START.getMessage()));
            session.getBasicRemote().sendText(JSONObject.toJSONString(result));
            return false;
        }
    }
}
