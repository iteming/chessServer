package com.example.chess.service;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.common.ChessAction;
import com.example.chess.common.Result;
import com.example.chess.common.RunContext;
import com.example.chess.common.room.ChessRoom;
import com.example.chess.common.room.Room;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/Catan")
@Component
public class WebsocketCatan {

    // 静态变量，用来记录当前在线链接数。应该把他设计成线程安全的。
    private static int onlineCount = 0;

    // 用来存放每个客户端对应的myWebSocket对象。
    private static CopyOnWriteArraySet<WebsocketCatan> user = new CopyOnWriteArraySet<>();

    private static ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

    // 客户端发送数据对象
    private Session session;

    static {
        System.out.println("Deamon Thread Created!");
        RunContext context = new RunContext(roomMap);
        DeamonThread deamonThread = new DeamonThread(context);
        Thread dThread = new Thread(deamonThread);
        dThread.start();
    }

    /**
     * 连接成功时调用的方法
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        user.add(this);

        // 发送消息给前端
        this.sendMessage("你分配的用户名：" + session.getId());
        // 在线数加1
        addOnlineCount();
        this.sendMessage("有新加入链接！当前在线人数为： --"+ getOnlineCount() +"--");

        System.out.println("Client connected");
    }

    /**
     * 连接关闭调用的方法
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        user.remove(this);

        // 在线人数减1
        subOnlineCount();
        this.sendMessage("有一连接关闭！当前在线人数为： --"+ getOnlineCount() +"--");

        String roomId = (String) session.getUserProperties().get("roomId");
        if (roomId != null) {
            Room room = roomMap.get(roomId);
            if (room != null) {
                room.leaveRoom(session);
                if (room.getNowNumber() <= 0) {
                    roomMap.remove(roomId);
                }
            }
        }
        System.out.println("Connection closed");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
        // 群发消息给前端
        for (WebsocketCatan myWebSocket: user){
            myWebSocket.session.getBasicRemote().sendText(session.getId());
        }

        // 控制台打印前端发送过来的消息
        System.out.println(message);


        Set<Session> session_list = session.getOpenSessions();
        String roomId = getRoomId(session);
        if (message.startsWith("connect")) {
            doConnect(session, message);
        } else if (message.startsWith("chess")) {
            String content = message.substring(5);
            ChessAction chessAction = JSONObject.parseObject(content, ChessAction.class);
            chessAction.setCode("Chess");
            Room room = roomMap.get(roomId);
            if (room.validAction(chessAction)) {
                Result result = new Result();
                result.setData(chessAction);
                room.broadcast(JSONObject.toJSONString(result));
            }
        } else if (message.startsWith("ready")) {
            doReady(session, message);
        } else if (message.startsWith("over")) {
            doReady(session, message);
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 给客户端传递消息
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        try {
            System.out.println("传递消息给前端：" + message);
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e){
            e.printStackTrace();
        }
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




    /**
     * 处理ready事件
     *
     * @param session
     * @param message
     * @throws IOException
     * @throws InterruptedException
     */
    private void doReady(Session session, String message) throws IOException, InterruptedException {
        Room room = getRoom(session);
        room.doReady(session);
    }

    private void doOver(Session session, String message) throws IOException, InterruptedException {
        Room room = getRoom(session);

    }

    /**
     * 获取RoomId
     *
     * @param session
     * @return
     */
    private String getRoomId(Session session) {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        return roomId;
    }

    /**
     * 获取房间
     *
     * @param session
     * @return
     */
    private Room getRoom(Session session) {
        return roomMap.get(getRoomId(session));
    }

    /**
     * 处理CONNECT请求
     *
     * @param session
     * @param message
     * @throws IOException
     * @throws InterruptedException
     */
    private void doConnect(Session session, String message) throws IOException, InterruptedException {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        System.out.println("A new Client Connect and the roomid is [" + roomId + "]");
        if (roomMap.containsKey(roomId)) {
            Room room = roomMap.get(roomId);
            if (room.enterRoom(session)) {
                session.getUserProperties().put("roomId", roomId);
            } else {
                System.out.println("---- 进入房间失败 ----");
                Result result = new Result();
                result.setCode(false);
                result.setMessage("进入房间失败");
                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
            }
        } else {
            Room room = new ChessRoom(roomId, 2);
            room.enterRoom(session);
            roomMap.put(roomId, room);
            session.getUserProperties().put("roomId", roomId);
        }
    }

}
