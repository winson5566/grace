package com.awinson.WebSocket.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by 10228 on 2016/12/19.
 */
@Service
public class WebSocketBaseServiceImpl implements WebSocketBaseService {


    @Autowired
    private SimpMessagingTemplate messaging;

    public void broadcast(String topic, String context) {
        WebSocketMessage message = new WebSocketMessage(context);
        messaging.convertAndSend("/topic/" + topic, message);
    }

    public void broadcastToUser(String username,String queue, String context) {
        WebSocketMessage message = new WebSocketMessage(context);
        messaging.convertAndSendToUser(username,"/queue/"+queue,message);
    }

}
