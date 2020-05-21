package com.example.chess.entity.base;

import com.example.chess.entity.play.UserContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chanming on 16/7/14.
 */
@Slf4j
public abstract class Room<T> {
    /**
     * 房间ID
     */
    protected @Getter
    @Setter
    String roomId;

    /**
     * 房间容纳总数
     */
    protected @Getter
    @Setter
    int totalNumber;

//    /**
//     * 现有人数
//     */
//    protected @Getter
//    @Setter
//    int nowNumber;

    /**
     * 创建时间
     */
    protected @Getter
    @Setter
    Date createTime;

    /**
     * 在游戏中的人员
     */
    protected @Getter
    @Setter
    Map<Session, UserContext<T>> sessions = new HashMap<Session, UserContext<T>>();

    /**
     * 初始化棋盘数据
     */
    public void initChessBoard() {
        System.out.println("Room[" + roomId + "] , chessBoard init ");
    }

    /**
     * broadcast 广播给所有用户
     */
    public void broadcast(String buffer, Session self) {
        for (Session session : sessions.keySet()) {
            try {
                if (self != null) {
                    if (self != session) {
                        session.getBasicRemote().sendText(buffer);
                    }
                } else {
                    session.getBasicRemote().sendText(buffer);
                }
            } catch (Exception e) {
                System.out.println("Error\n");
                log.error("Error\n" + e);
            }
        }
    }

    /**
     * 房间进满了人事件
     */
    public void fullEvent() {
        System.out.println("Room[" + roomId + "] is full, Send Ready Message");
    }

    /**
     * 进入房间
     */
    public boolean enterRoom(Session session) {
        if (!isFull()) {
            sessions.put(session, new UserContext<T>(session));
//            nowNumber++;

            // 所有用户都已进入房间后，广播房间已满消息
            if (isFull()) {
                fullEvent();
            }
            return true;
        }

        return false;
    }

    /**
     * 离开房间
     */
    public void leaveRoom(Session session) {
        sessions.remove(session);
//        nowNumber--;
    }


    /**
     * 用户准备开始游戏
     */
    public void doReady(Session session) {
        if (session == null) {
            // 所有用户开始准备
            for (Map.Entry<Session, UserContext<T>> entry : sessions.entrySet()) {
                entry.getValue().setGameStatus(UserContext.GAME_STATUS.READY);
            }
            if (checkAllReady()) {
                startGame();
            }
        } else {
            UserContext<T> userContext = sessions.get(session);
            userContext.setGameStatus(UserContext.GAME_STATUS.READY);
            if (checkAllReady()) {
                startGame();
            }
        }
    }

    /**
     * 用户取消准备
     */
    public void doOver(Session session) {
        if (session == null) {
            // 所有用户取消准备
            for (Map.Entry<Session, UserContext<T>> entry : sessions.entrySet()) {
                entry.getValue().setGameStatus(UserContext.GAME_STATUS.PENDING);
            }
        } else {
            UserContext<T> userContext = sessions.get(session);
            userContext.setGameStatus(UserContext.GAME_STATUS.PENDING);
        }
    }


    /**
     * 游戏开始事件
     */
    public void startGame() {
        for (Map.Entry<Session, UserContext<T>> each : sessions.entrySet()) {
            each.getValue().setGameStatus(UserContext.GAME_STATUS.RUNNING);
        }
    }

    /**
     * 检查所有的选手是否已经准备完成
     */
    private boolean checkAllReady() {
        if (!isFull()) {
            return false;
        }
        for (Map.Entry<Session, UserContext<T>> each : sessions.entrySet()) {
            if (!each.getValue().isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个ACTION是否合法
     */
    public boolean validAction(Action action) {
        return true;
    }

    /**
     * 判断房间是否已经满了
     */
    public boolean isFull() {
//        return !(nowNumber < totalNumber);
        return !(sessions.size() < totalNumber);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Room room = (Room) o;

        return roomId.equals(room.roomId);
    }

    @Override
    public int hashCode() {
        return roomId.hashCode();
    }
}
