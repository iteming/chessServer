package com.example.chess.entity.catan;

import com.alibaba.fastjson.JSONObject;
import com.example.chess.entity.base.Action;
import com.example.chess.entity.base.Result;
import com.example.chess.entity.base.Room;
import com.example.chess.entity.catan.constant.KeyValueMap;
import com.example.chess.entity.catan.constant.PropertyEnum;
import com.example.chess.entity.catan.constant.TurnSortEnum;
import com.example.chess.entity.catan.constant.UserStatusEnum;
import com.example.chess.entity.play.UserContext;
import com.example.chess.protocol.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;


@Slf4j
public class CatanRoom extends Room<CatanPlayer> {

    /**
     * 当前是第几轮
     */
    private int localTurn = 0;

    /**
     * 7 * 7 的棋盘，第一个 7 确实为 7
     * 第二个 7 是递增递减，分别为 4，5，6，7，6，5，4
     */
    private ChessBoard[][] chessBoards = new ChessBoard[7][7];

    /**
     * 棋盘所有数字
     */
    private Integer allChees[] = {5, 10, 8, 2, 9, 11, 4, 6, 4, -1, 3, 11, 3, 5, 6, 12, 8, 10, 9};

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
        // 构建棋盘
        chessBoards[0][0] = chessWater(0);
        chessBoards[0][1] = chessWater(1);
        chessBoards[0][2] = chessWater(2);
        chessBoards[0][3] = chessWater(3);


        // 第二列
        chessBoards[1][0] = chessWater(10);
        chessBoards[1][1] = new ChessBoard(11, PropertyEnum.TREE, allChees[0], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(3, PropertyEnum.ANY_THING));
            this.add(new KeyValueMap(6, PropertyEnum.WHEAT));
        }}));
        chessBoards[1][2] = new ChessBoard(12, PropertyEnum.WHEAT, allChees[1], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(1, PropertyEnum.WHEAT));
            this.add(new KeyValueMap(2, PropertyEnum.WHEAT));
        }}));
        chessBoards[1][3] = new ChessBoard(13, PropertyEnum.BRICK, allChees[2], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(1, PropertyEnum.ANY_THING));
            this.add(new KeyValueMap(6, PropertyEnum.ANY_THING));
        }}));
        chessBoards[1][4] = chessWater(14);


        // 第三列
        chessBoards[2][0] = chessWater(20);
        chessBoards[2][1] = new ChessBoard(21, PropertyEnum.WHEAT, allChees[3], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(1, PropertyEnum.STONE));
            this.add(new KeyValueMap(2, PropertyEnum.STONE));
        }}));
        chessBoards[2][2] = new ChessBoard(22, PropertyEnum.BRICK, allChees[4], null);
        chessBoards[2][3] = new ChessBoard(23, PropertyEnum.STONE, allChees[5], null);
        chessBoards[2][4] = new ChessBoard(24, PropertyEnum.SHEEP, allChees[6], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(5, PropertyEnum.TREE));
            this.add(new KeyValueMap(6, PropertyEnum.TREE));
        }}));
        chessBoards[2][5] = chessWater(25);


        // 第四列
        chessBoards[3][0] = chessWater(30);
        chessBoards[3][1] = new ChessBoard(31, PropertyEnum.TREE, allChees[7], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(2, PropertyEnum.ANY_THING));
            this.add(new KeyValueMap(3, PropertyEnum.ANY_THING));
        }}));
        chessBoards[3][2] = new ChessBoard(32, PropertyEnum.STONE, allChees[8], null);
        chessBoards[3][3] = new ChessBoard(33, PropertyEnum.DESERT, allChees[9], null);
        chessBoards[3][4] = new ChessBoard(34, PropertyEnum.TREE, allChees[10], null);
        chessBoards[3][5] = new ChessBoard(35, PropertyEnum.BRICK, allChees[11], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(1, PropertyEnum.TREE));
            this.add(new KeyValueMap(4, PropertyEnum.BRICK));
        }}));
        chessBoards[3][6] = chessWater(36);


        // 第五列
        chessBoards[4][0] = chessWater(40);
        chessBoards[4][1] = new ChessBoard(41, PropertyEnum.SHEEP, allChees[12], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(3, PropertyEnum.SHEEP));
            this.add(new KeyValueMap(4, PropertyEnum.SHEEP));
        }}));
        chessBoards[4][2] = new ChessBoard(42, PropertyEnum.WHEAT, allChees[13], null);
        chessBoards[4][3] = new ChessBoard(43, PropertyEnum.STONE, allChees[14], null);
        chessBoards[4][4] = new ChessBoard(44, PropertyEnum.SHEEP, allChees[15], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(5, PropertyEnum.BRICK));
            this.add(new KeyValueMap(6, PropertyEnum.BRICK));
        }}));
        chessBoards[4][5] = chessWater(45);


        // 第六列
        chessBoards[5][0] = chessWater(50);
        chessBoards[5][1] = new ChessBoard(51, PropertyEnum.SHEEP, allChees[16], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(2, PropertyEnum.SHEEP));
            this.add(new KeyValueMap(5, PropertyEnum.ANY_THING));
        }}));
        chessBoards[5][2] = new ChessBoard(52, PropertyEnum.TREE, allChees[17], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(3, PropertyEnum.ANY_THING));
            this.add(new KeyValueMap(4, PropertyEnum.ANY_THING));
        }}));
        chessBoards[5][3] = new ChessBoard(53, PropertyEnum.WHEAT, allChees[18], ChessBoard.initNode(new ArrayList<KeyValueMap>() {{
            this.add(new KeyValueMap(4, PropertyEnum.ANY_THING));
            this.add(new KeyValueMap(5, PropertyEnum.ANY_THING));
        }}));
        chessBoards[5][4] = chessWater(54);


        chessBoards[6][0] = chessWater(60);
        chessBoards[6][1] = chessWater(61);
        chessBoards[6][2] = chessWater(62);
        chessBoards[6][3] = chessWater(63);
    }

    private ChessBoard chessWater(Integer id) {
        return new ChessBoard(id, PropertyEnum.WATER, 0, null);
    }

    private void initChessUser() {
        // 颜色数组
        String[] colorList = {"red", "yellow", "green"};
        // 序号
        int sortNumber = 0;

        // 初始化用户信息
        for (Map.Entry<Session, UserContext<CatanPlayer>> entry : sessions.entrySet()) {
            // 玩家
            CatanPlayer catanPlayer = entry.getValue().getPlayer();
            if (catanPlayer == null) {
                catanPlayer = new CatanPlayer();
            }
            catanPlayer.setStatus(UserStatusEnum.WAIT);
            catanPlayer.setIsDone(true); // TODO: 全都执行过了，这样方便开始第一轮循环
            catanPlayer.setNumber(sortNumber);
            catanPlayer.setColor(colorList[sortNumber]);
            catanPlayer.setRoundBuildLongRoad(0);
            catanPlayer.setRoundPoints(0);
            catanPlayer.setRoundUsedDevelopCards(0);
            catanPlayer.setChessEntity(new ArrayList<>());
            catanPlayer.setResources(new ArrayList<>());
            entry.getValue().setPlayer(catanPlayer);
        }
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
    @Override
    public void startTurn() {
        isNextTurn();

        ArrayList<Map.Entry<Session, UserContext<CatanPlayer>>> listUser = new ArrayList<>(sessions.entrySet());
        if (initRoundRule() == TurnSortEnum.DESC) {
            // 逆序
            Collections.reverse(listUser);
        }

        // 循环查找下一个未执行的人，让其开始执行，其他人等待
        for (Map.Entry<Session, UserContext<CatanPlayer>> entry : listUser) {
            // 查找第一个未执行的人 （回合结束时，要重置所有人的isDone）
            if (!entry.getValue().getPlayer().getIsDone()) {
                // 修改当前人状态为，等待投掷筛子
                entry.getValue().getPlayer().setStatus(UserStatusEnum.THROW_SIEVE);

                // 通知当前用户投掷筛子
                sendMessage(commonResult(ActionTypeEnum.TURN_ROUND_THROW_SIEVE), entry.getKey());
                // 其他人待命
                broadcast(commonResult(ActionTypeEnum.TURN_ROUND), entry.getKey());
                break;
            }
        }
    }

    /**
     * 接收用户的一个动作
     */
    @Override
    public void startAction(ActionTypeEnum actionTypeEnum) {
        log.debug("do action enum is {},{} ", actionTypeEnum.getCode(), actionTypeEnum.getMessage());
    }

    private String commonResult(ActionTypeEnum actionTypeEnum) {
        Result result = Result.success(new Result(actionTypeEnum.getCode(), actionTypeEnum.getMessage()));
        return JSONObject.toJSONString(result);
    }

    private Boolean sendMessage(String message, Session session) {
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
            if (!entry.getValue().getPlayer().getIsDone()) {
                isNext = false;
                break;
            }
        }

        if (isNext) {
            localTurn++;
            // 全都执行过了，则恢复所有用户的状态
            for (Map.Entry<Session, UserContext<CatanPlayer>> entry : sessions.entrySet()) {
                entry.getValue().getPlayer().setIsDone(false);
            }
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
