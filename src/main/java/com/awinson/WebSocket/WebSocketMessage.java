package com.awinson.WebSocket;

/**
 * Created by winson on 2016/12/17.
 */
public class WebSocketMessage {
    private String content;

    public WebSocketMessage() {
    }

    public WebSocketMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
