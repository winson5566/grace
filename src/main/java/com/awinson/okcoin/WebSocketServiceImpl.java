package com.awinson.okcoin;

import com.awinson.WebSocket.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by 10228 on 2016/12/19.
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate messaging;

    public void broadcast(String topic, String context) {
        WebSocketMessage message = new WebSocketMessage(context);
        messaging.convertAndSend("/topic/" + topic, message);
    }

    @Override
    public void getUserInfo() {
        String apiKey = "653e8232-ab6e-4523-9310-514d8f2d8f13";
        String secretKey = "9E438027B165666CBA74DED24D6F3C9D";
        String url = "wss://real.okcoin.cn:10440/websocket/okcoinapi";   // 国内站WebSocket地址
        WebSoketClient client = new WebSoketClient(url, webSocketService);  // WebSocket客户端
        client.start();  // 启动客户端


        // 添加订阅
        client.addChannel("ok_sub_spotcny_btc_ticker");

        // 获取用户信息
        client.getUserInfo(apiKey,secretKey);
    }

    @Override
    public void onReceive(String msg) {
        logger.info("WebSocket Client received message: " + msg);
    }


}
