package com.example.chess.service;

import com.example.chess.entity.base.Room;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
@Component
public class WebsocketTest {

    // 静态变量，用来记录当前在线链接数。应该把他设计成线程安全的。
    private static int onlineCount = 0;

    // 用来存放每个客户端对应的myWebSocket对象。
    private static CopyOnWriteArraySet<WebsocketTest> user = new CopyOnWriteArraySet<>();

    private static ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<String, Room>();

    // 客户端发送数据对象
    private Session session;

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
        for (WebsocketTest myWebSocket: user){
            myWebSocket.session.getBasicRemote().sendText(session.getId());
        }

        // 控制台打印前端发送过来的消息
        System.out.println(message);
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
        WebsocketTest.onlineCount++;
    }
    private static synchronized void subOnlineCount() {
        WebsocketTest.onlineCount--;
    }
}
