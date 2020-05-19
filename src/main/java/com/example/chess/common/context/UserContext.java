package com.example.chess.common.context;

import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;


/**
 * Created by chanming on 16/7/29.
 */

public class UserContext {

    private Session session;

    public UserContext(Session session) {
        this.session = session;
    }

    /**
     * 游戏状态
     */
    private @Getter @Setter int gameStatus;

    public interface GAME_STATUS {
        /**
         * 待处理
         */
        int PENDING = 0;
        /**
         * 已准备
         */
        int READY = 1;
        /**
         * 运行中
         */
        int RUNNING = 2;
    }

    /**
     * 是否准备完成
     */
    public boolean isReady() {
        return gameStatus == GAME_STATUS.READY;
    }
}
