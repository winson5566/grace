package com.awinson.WebSocket.okcoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by 10228 on 2016/12/24.
 */
@Service
public class WebSocketInit {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketInit.class);

    @Autowired
    private OkcoinWebSocketService webSocketService;

    @PostConstruct
    public void init() {
        okcoinInit();
    }

    /**
     * 订阅OkcoinCN的BTC和LTC价格
     */
    private void okcoinInit() {

        //OkcoinCN的websock注册地址
        String url = "wss://real.okcoin.cn:10440/websocket/okcoinapi";   // 国内站WebSocket地址

        //注册客户端
        WebSoketClient client = new WebSoketClient(url, webSocketService);  // WebSocket客户端

        //启动客户端
        client.start();

        // 添加订阅
        client.addChannel("ok_sub_spotcny_btc_ticker");
        client.addChannel("ok_sub_spotcny_ltc_ticker");

        // 获取用户信息
        //client.getUserInfo(apiKey,secretKey);

    }

}
