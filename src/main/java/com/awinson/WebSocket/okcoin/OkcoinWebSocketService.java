package com.awinson.WebSocket.okcoin;

/**
 * Created by 10228 on 2016/12/19.
 */
public interface OkcoinWebSocketService {

    /**
     * 消息接受处理
     * @param msg
     */
    void onReceive(String msg);
}
