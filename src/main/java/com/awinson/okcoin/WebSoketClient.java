package com.awinson.okcoin;

/**
 * Created by 10228 on 2016/12/24.
 */
public class WebSoketClient  extends WebSocketBase {
    String apiKey = "653e8232-ab6e-4523-9310-514d8f2d8f13";
    String secretKey = "9E438027B165666CBA74DED24D6F3C9D";
    String url = "wss://real.okcoin.cn:10440/websocket/okcoinapi";   // 国内站WebSocket地址

    public WebSoketClient(String url,WebSocketService service){
        super(url,service);
    }

    WebSoketClient client = new WebSoketClient(url, webSocketService);  // WebSocket客户端
        client.start();  // 启动客户端
}
