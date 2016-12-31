package com.awinson.WebSocket.okcoin;

/**
 * Created by 10228 on 2016/12/24.
 */
public class WebSoketClient  extends WebSocketBase {

    /**
     * 注册客户端（URL和消息接收的处理类）
     * @param url
     * @param service
     */
    public WebSoketClient(String url,OkcoinWebSocketService service){
        super(url,service);
    }


}
