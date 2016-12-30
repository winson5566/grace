package com.awinson.WebSocket.okcoin;

/**
 * Created by 10228 on 2016/12/19.
 */
public interface OkcoinWebSocketService {
    /**
     * 通用的WebSocket推送服务
     * @param topic
     * @param context
     */
    void broadcast(String topic, String context);


    public void onReceive(String msg);
}
