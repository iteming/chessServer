package com.example.chess.entity.five;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.base.Action;
import com.example.chess.entity.base.ChessResult;
import com.example.chess.entity.base.Room;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Null;
import javax.websocket.Session;
import java.util.Date;

/**
 * Created by chanming on 16/7/14.
 */
@Slf4j
public class FiveRoom extends Room<Null> {

    private Integer[][] chessBoard = new Integer[16][16];
    private int maxSize = 16;
    private FiveAction lastFiveAction;

    public FiveRoom(String roomId, int totalNumber) {
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
                ChessResult chessResult = new ChessResult();
                startAction.setCode("fullEvent");
                chessResult.setData(startAction);
                session.getBasicRemote().sendText(JSONObject.toJSONString(chessResult));
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
                ChessResult chessResult = new ChessResult();
                chessResult.setData(startAction);
                session.getBasicRemote().sendText(JSONObject.toJSONString(chessResult));
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
        if (action instanceof FiveAction) {
            if (lastFiveAction != null && ((FiveAction) action).getColor().equals(lastFiveAction.getColor())) {
                return false;
            }
            if (chessBoard[((FiveAction) action).getX()][((FiveAction) action).getY()] != 0) {
                return false;
            }
            chessBoard[((FiveAction) action).getX()][((FiveAction) action).getY()] = ((FiveAction) action).getColor().equals("Black") ? -1 : 1;
            return true;
        }
        return false;
    }

}
