package com.awinson.WebSocket.web;

/**
 * Created by 10228 on 2016/12/19.
 */
public interface WebSocketBaseService {
    /**
     * WebSocket广播服务（topic）
     * @param topic
     * @param context
     */
    void broadcast(String topic,String context);

    /**
     * WebSocket指定用户推送服务(queue)
     */
    void broadcastToUser(String username,String queue, String context);
}
