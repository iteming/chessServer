package com.example.chess.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.base.ChessResult;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.five.FiveAction;
import com.example.chess.entity.five.FiveRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chanming on 16/7/13.
 * 12
 */

@ServerEndpoint("/chess")
@Component
public class WebSocketChess {

    private static Logger logger = LoggerFactory.getLogger(WebSocketChess.class);

    private static Integer total = 0;

    private static ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

//    static {
//        System.out.println(" chess static ");
//        RunContext context = new RunContext(roomMap);
//        DeamonThread deamonThread = new DeamonThread(context);
//        Thread dThread = new Thread(deamonThread);
//        logger.info("Deamon Thread Created!");
//        dThread.start();
//    }

    @OnMessage
    public void onMessage(String message, Session session)
            throws IOException, InterruptedException {
        Set<Session> session_list = session.getOpenSessions();
        String roomId = getRoomId(session);
        if (message.startsWith("connect")) {
            doConnect(session, message);
        } else if (message.startsWith("chess")) {
            String content = message.substring(5);
            FiveAction fiveAction = JSONObject.parseObject(content, FiveAction.class);
            fiveAction.setCode("Chess");
            Room room = roomMap.get(roomId);
            if (room.validAction(fiveAction)) {
                ChessResult chessResult = new ChessResult();
                chessResult.setData(fiveAction);
                room.broadcast(JSONObject.toJSONString(chessResult), null);
            }
        } else if (message.startsWith("ready")) {
            doReady(session, message);
        } else if (message.startsWith("over")) {
            doOver(session, message);
        }
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
        room.doOver(session);
        // 重新初始化棋盘
        room.initChessBoard();
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
    public void doConnect(Session session, String message) throws IOException, InterruptedException {
        List<String> roomList = session.getRequestParameterMap().get("roomId");
        String roomId = roomList.get(0);
        logger.info("A new Client Connect and the roomid is [" + roomId + "]");
        if (roomMap.containsKey(roomId)) {
            Room room = roomMap.get(roomId);
            if (room.enterRoom(session)) {
                session.getUserProperties().put("roomId", roomId);
            } else {
                System.out.println("---- 进入房间失败 ----");
                ChessResult chessResult = new ChessResult();
                chessResult.setCode(false);
                chessResult.setMessage("进入房间失败");
                session.getBasicRemote().sendText(JSONObject.toJSONString(chessResult));
            }
        } else {
            Room room = new FiveRoom(roomId, 2);
            room.enterRoom(session);
            roomMap.put(roomId, room);
            session.getUserProperties().put("roomId", roomId);
        }
    }


    @OnOpen
    public void onOpen() {
        System.out.println("Client connected");
    }

    @OnClose
    public void onClose(Session session) {
        String roomId = (String) session.getUserProperties().get("roomId");
        if (roomId != null) {
            Room room = roomMap.get(roomId);
            if (room != null) {
                room.leaveRoom(session);
                if (room.getSessions().size() <= 0) {
                    roomMap.remove(roomId);
                }
            }
        }
        System.out.println("Connection closed");
    }
}
