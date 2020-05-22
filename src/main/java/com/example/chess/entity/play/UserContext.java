package com.example.chess.entity.play;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by chanming on 16/7/29.
 */

public class UserContext<T> {

    private @Getter @Setter T player;
    private @Getter @Setter int gameStatus;

    public UserContext(T player) {
        this.player = player;
    }

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
    public boolean isReady() {
        return gameStatus == GAME_STATUS.READY;
    }
}
