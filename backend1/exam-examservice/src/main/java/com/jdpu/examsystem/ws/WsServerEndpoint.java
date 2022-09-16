package com.jdpu.examsystem.ws;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * code = 1 : 在线用户列表
 * code = 2 : 文本信息
 * code = 3 : 图片信息
 */

@ServerEndpoint(value = "/exam",configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class WsServerEndpoint {
    /**
     * 存每一个用户的WsServerEndpoint对象
     */
    private static Map<String,WsServerEndpoint> onlineUsers = new ConcurrentHashMap<>();

    private Session session;
    private HttpSession httpSession;

    /**
     * 连接成功
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;

        System.out.println("open有session吗"+session);

        //获取httpsession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;

        System.out.println("open有httpSession吗"+httpSession);

        //获取httpsession域中存放的username对应的值
        String username = (String) httpSession.getAttribute("username");
        log.info("用户："+username+" 连接成功");
        //存放到onlineUsers中保存
        onlineUsers.put(username, this);
        broadcastMsgToAllOnlineUsers();
    }

    /**
     * 广播方法
     */
    private void broadcastMsgToAllOnlineUsers() {
        //所有登录用户名称
        Set<String> names = onlineUsers.keySet();
        for (String name : names) {
            WsServerEndpoint wsServerEndpoint = onlineUsers.get(name);
            //获取推送对象
            RemoteEndpoint.Basic basicRemote = wsServerEndpoint.session.getBasicRemote();
            try {
                String message = "";
                Map<String, Object> result = new HashMap<>();
                result.put("code","1");
                result.put("msg",names);
                message = JSONUtil.toJsonStr(result);
                basicRemote.sendText(message);
            } catch (IOException e) {
                log.error("广播发送系统消息失败！{}", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        String username = (String) httpSession.getAttribute("username");
        log.info(username + "：连接关闭");
        WsServerEndpoint remove = onlineUsers.remove(username);
        broadcastMsgToAllOnlineUsers();
    }

    /**
     * 接收到消息
     * @param text
     */
    @OnMessage
    public void onMsg(String text,Session session) {

        System.out.println("msg有session吗"+session);

        // 1. 从session获取当前用户名
        // 2. 获取要发送给谁
        // 3. 发送消息
        // onlineUsers.get(toName).session.getBasicRemote().sendText(sendMsg);

        JSONObject jsonObject = JSONUtil.parseObj(text);
        String code = jsonObject.getStr("code");
        String toName = jsonObject.getStr("toName");
        String msg = jsonObject.getStr("msg");
        String examInfoId = jsonObject.getStr("examInfoId");

        // 信息发送方
        String curUsername = (String) httpSession.getAttribute("username");

        // 最终发送的信息
        String finalMsg = "";

        // 往Map存入返回信息封装到finalMsg
        // 发送文本消息
        // 暂时没用
        if (code.equals("2")) {
            Map<String, String> result = new HashMap<>();
            result.put("code","2");
            result.put("fromName",curUsername);
            result.put("msg",msg);
            result.put("examInfoId",examInfoId);
            finalMsg = JSONUtil.toJsonStr(result);
        }

        // 发送图片信息
        if (code.equals("3")) {
            log.info("抓拍学生"+curUsername+"图片");
            Map<String, String> result = new HashMap<>();
            result.put("code","3");
            result.put("fromName",curUsername);
            result.put("msg",msg);
            result.put("examInfoId",examInfoId);
            finalMsg = JSONUtil.toJsonStr(result);
        }

        // 警告信息
        if ("4".equals(code)) {
            log.info("警告学生："+toName+",警告信息为："+msg);
            Map<String, String> result = new HashMap<>();
            result.put("code","4");
            result.put("fromName",curUsername);
            result.put("msg",msg);
            result.put("examInfoId",examInfoId);
            finalMsg = JSONUtil.toJsonStr(result);
        }

        // 强制收卷信息
        if ("5".equals(code)) {
            log.info("学生："+toName+"被"+msg);
            Map<String, String> result = new HashMap<>();
            result.put("code","5");
            result.put("fromName",curUsername);
            result.put("msg",msg);
            result.put("examInfoId",examInfoId);
            finalMsg = JSONUtil.toJsonStr(result);
        }

        try {
            if(onlineUsers.containsKey(toName)) {
                onlineUsers.get(toName).session.getBasicRemote().sendText(finalMsg);
            } else {
                log.error("websocket此用户不存在 toName:"+toName);
            }
        } catch (IOException e) {
            log.error(e.getMessage()+",toName="+toName);
            e.printStackTrace();
        }

        // // 发送信息
        // try {
        //     onlineUsers.get(toName).session.getBasicRemote().sendText(finalMsg);
        // } catch (Exception e) {
        //     log.error(e.getMessage()+",toName="+toName);
        //     //e.printStackTrace();
        // }
    }
}
