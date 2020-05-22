package com.example.chess.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractWebSocketServer {

    // 用来存放每个客户端对应的AbstractWebSocketServer对象。
    // 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static Map<String, AbstractWebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    protected Session session;

    protected String param;

    /**
     * <b>功能描述：</b>连接关闭/发生错误时调用的方法<br>
     * <b>修订记录：</b><br>
     * <li>20190620&nbsp;&nbsp;|&nbsp;&nbsp;邓冲&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @OnClose
    public void onClose() {
        //从set中移除
        webSocketMap.remove(this.param);
    }

    /**
     * <b>功能描述：</b>收到客户端消息后调用的方法<br>
     * <b>修订记录：</b><br>
     * <li>20190620&nbsp;&nbsp;|&nbsp;&nbsp;邓冲&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @OnMessage
    public void onMessage(String message) {
        log.debug("客户端发的消息内容为：" + message);
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * <b>功能描述：</b>点对点消息<br>
     * <b>修订记录：</b><br>
     * <li>20190624&nbsp;&nbsp;|&nbsp;&nbsp;邓冲&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public <T> void sendMessage(String param, T t) {
        log.debug("【websocket消息】单点消息:" + t);
        AbstractWebSocketServer socketServer = webSocketMap.get(param);
        if (socketServer != null) {
            try {
                String content = JSONObject.toJSONString(t);
                socketServer.session.getAsyncRemote().sendText(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <b>功能描述：</b>点对点消息<br>
     * <b>修订记录：</b><br>
     * <li>20190624&nbsp;&nbsp;|&nbsp;&nbsp;邓冲&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    public  <T> void sendAllMessage(T t) {
        log.debug("【websocket消息】群发消息:" + t);
        for (AbstractWebSocketServer socketServer : webSocketMap.values()) {
            try {
                socketServer.session.getAsyncRemote().sendText(t.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <b>功能描述：</b>发生错误时调用<br>
     * <b>修订记录：</b><br>
     * <li>20190620&nbsp;&nbsp;|&nbsp;&nbsp;邓冲&nbsp;&nbsp;|&nbsp;&nbsp;创建方法</li><br>
     */
    @OnError
    public void onError(Session session, Throwable e) {
        log.error(session.getId() + " socket occur error", e);
        e.printStackTrace();
    }

    public static Map<String, AbstractWebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }
}
