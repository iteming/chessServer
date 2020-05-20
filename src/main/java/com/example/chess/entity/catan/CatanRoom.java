package com.example.chess.entity.catan;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.base.Action;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.play.UserContext;
import com.example.chess.protocol.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Date;
import java.util.Map;

@Slf4j
public class CatanRoom extends Room<CatanPlayer> {

    /**
     * 当前是第几轮
     */
    private int localTurn = 0;

    public CatanRoom(String roomId, int totalNumber) {
        this.roomId = roomId;
        this.totalNumber = totalNumber;
        this.createTime = new Date();
    }

    /**
     * 初始化棋盘数据
     */
    @Override
    public void initChessBoard() {
//        for (int i = 0; i < maxSize; ++i) {
//            for (int j = 0; j < maxSize; ++j) {
//                chessBoard[i][j] = 0;
//            }
//        }
    }

    private void initChessUser() {
//        for (Session session : sessions.keySet()) {
//            try {
//                ChessResult chessResult = new ChessResult();
//                chessResult.setData(startAction);
//                session.getBasicRemote().sendText(JSONObject.toJSONString(chessResult));
//                log.info("Send Start Message OK");
//            } catch (Exception e) {
//                log.error("Send Start Message Error");
//            }
//            tmp++;
//        }
    }

    /**
     * 初始化循环规则
     * 比如第二次循环逆序，其他都是正序
     */
    private TurnSortEnum initRoundRule() {
        if (localTurn == 2) {
            return TurnSortEnum.DESC;
        }
        return TurnSortEnum.ASC;
    }

    /**
     * 房间进满了人事件
     */
    @Override
    public void fullEvent() {
        System.out.println("Room[" + roomId + "] is full, Send Ready Message");
        for (Session session : sessions.keySet()) {
            try {
                Result result = Result.failed(new Result(ActionTypeEnum.FULL_MSG.getCode(), ActionTypeEnum.FULL_MSG.getMessage()));
                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                System.out.println("Send OK");
            } catch (Exception e) {
                System.out.println("SendText Error" + e.getMessage());
            }
        }
    }

    @Override
    public void startGame() {
        // 初始化棋盘，初始化用户数据
        initChessBoard();
        initChessUser();

        log.info("Room[" + roomId + "] is allReady, Send GameStart Message!");
        super.startGame();

        broadcast(commonResult(ActionTypeEnum.GAME_START), null);

        // 开始一个回合
        startTurn();
    }

    /**
     * 开始一个回合
     */
    private void startTurn() {

        isNextTurn();
        if (initRoundRule() == TurnSortEnum.ASC){
            // 正序

            // 循环查找下一个未执行的人，让其开始执行，其他人等待
            for (Map.Entry<Session, UserContext<CatanPlayer>> entry : sessions.entrySet()) {
                // 查找第一个未执行的人 （回合结束时，要重置所有人的isDone）
                if (!entry.getValue().getPlayer().getIsDone()){

                    // 修改当前人状态为，等待投掷筛子
                    entry.getValue().getPlayer().setStatus(UserStatusEnum.THROW_SIEVE);
                    // 通知当前用户投掷筛子
                    sendMessage(commonResult(ActionTypeEnum.TURN_ROUND_THROW_SIEVE), entry.getKey());
                    // 其他人待命
                    broadcast(commonResult(ActionTypeEnum.TURN_ROUND), entry.getKey());
                    break;
                }
            }
        } else {
            // 逆序

        }
    }

    /**
     * 接收用户的一个动作
     */
    private void startAction(){

    }

    private String commonResult(ActionTypeEnum actionTypeEnum){
        Result result = Result.success(new Result(actionTypeEnum.getCode(), actionTypeEnum.getMessage()));
        return JSONObject.toJSONString(result);
    }

    private Boolean sendMessage(String message, Session session){
        try {
            session.getBasicRemote().sendText(message);
            log.info("Send Start Message OK");
            return true;
        } catch (Exception e) {
            log.error("Send Start Message Error");
            return false;
        }
    }

    private void isNextTurn() {
        boolean isNext = true;

        for (Map.Entry<Session, UserContext<CatanPlayer>> entry : sessions.entrySet()) {
            entry.getValue().setGameStatus(UserContext.GAME_STATUS.READY);
            if (!entry.getValue().getPlayer().getIsDone()){
                isNext = false;
                break;
            }
        }

        if (isNext) {
            localTurn++;
        }
    }

    @Override
    public void doOver(Session s) {
        if (s == null) {
            super.doOver(null);
            for (Session session : sessions.keySet()) {
                try {
                    Result result = Result.success(new Result(ActionTypeEnum.GAME_END.getCode(), ActionTypeEnum.GAME_END.getMessage()));
                    session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                    log.info("Send Start Message OK");
                } catch (Exception e) {
                    log.error("Send Start Message Error");
                }
            }
        }
    }

    @Override
    public boolean validAction(Action action) {
//        if (action instanceof FiveAction) {
//            if (lastFiveAction != null && ((FiveAction) action).getColor().equals(lastFiveAction.getColor())) {
//                return false;
//            }
//            if (chessBoard[((FiveAction) action).getX()][((FiveAction) action).getY()] != 0) {
//                return false;
//            }
//            chessBoard[((FiveAction) action).getX()][((FiveAction) action).getY()] = ((FiveAction) action).getColor().equals("Black") ? -1 : 1;
//            return true;
//        }
        return false;
    }
}
