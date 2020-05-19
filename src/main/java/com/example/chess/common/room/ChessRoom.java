package com.example.chess.common.room;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.common.Action;
import com.example.chess.common.ChessAction;
import com.example.chess.common.Result;
import com.example.chess.common.StartAction;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Date;

/**
 * Created by chanming on 16/7/14.
 */
@Slf4j
public class ChessRoom extends Room {

    private Integer[][] chessBoard = new Integer[16][16];
    private int maxSize = 16;
    private ChessAction lastChessAction;

    public ChessRoom(String roomId, int totalNumber) {
        this.roomId = roomId;
        this.totalNumber = totalNumber;
        this.createTime = new Date();
        initChessBoard();
    }

    /**
     * 初始化棋盘数据
     */
    @Override
    public void initChessBoard() {
        for (int i = 0; i < maxSize; ++i) {
            for (int j = 0; j < maxSize; ++j) {
                chessBoard[i][j] = 0;
            }
        }
    }

    /**
     * 房间进满了人事件
     */
    @Override
    public void fullEvent() {
        System.out.println("Room[" + roomId + "] is full, Send Ready Message");
        int tmp = 0;
        for (Session session : sessions.keySet()) {
            StartAction startAction = new StartAction();
            if (tmp == 0) {
                startAction.setDetail("Black");
            } else {
                startAction.setDetail("White");
            }
            try {
                Result result = new Result();
                startAction.setCode("fullEvent");
                result.setData(startAction);
                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                System.out.println("Send OK");
            } catch (Exception e) {
                System.out.println("SendText Error" + e.getMessage());
            }
            tmp++;
        }
    }

    @Override
    public void startGame() {
        log.info("Room[" + roomId + "] is allReady, Send GameStart Message!");
        int tmp = 0;
        for (Session session : sessions.keySet()) {
            StartAction startAction = new StartAction();
            if (tmp == 0) {
                startAction.setDetail("Black");
            } else {
                startAction.setDetail("White");
            }
            try {
                Result result = new Result();
                result.setData(startAction);
                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                log.info("Send Start Message OK");
            } catch (Exception e) {
                log.error("Send Start Message Error");
            }
            tmp++;
        }
        super.startGame();
    }

    @Override
    public boolean validAction(Action action) {
        if (action instanceof ChessAction) {
            if (lastChessAction != null && ((ChessAction) action).getColor().equals(lastChessAction.getColor())) {
                return false;
            }
            if (chessBoard[((ChessAction) action).getX()][((ChessAction) action).getY()] != 0) {
                return false;
            }
            chessBoard[((ChessAction) action).getX()][((ChessAction) action).getY()] = ((ChessAction) action).getColor().equals("Black") ? -1 : 1;
            return true;
        }
        return false;
    }

}
