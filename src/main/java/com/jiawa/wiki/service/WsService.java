package com.jiawa.wiki.service;

import com.jiawa.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WsService {

    @Autowired
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String msssage,String logId){
        // 推送消息
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(msssage);
    }
}
