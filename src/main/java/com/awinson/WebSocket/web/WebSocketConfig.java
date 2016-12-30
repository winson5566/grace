package com.awinson.WebSocket.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
/**
 * Created by winson on 2016/12/17.
 */

@Configuration
@EnableWebSocketMessageBroker   //①配置WebSocket②配置基于代理的STOMP消息
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {  //配置消息代理
        config.enableSimpleBroker("/topic","/queue");    //缺省即为topic,这里消息代理将会处理前缀为"/topic"和"/queue"的消息
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {    //注册STOMP端点，客户端在订阅或发布消息到目的地路径前，要连接该端点。
        registry.addEndpoint("/gs-guide-websocket").withSockJS();   //为"/gs-guide-websocket"路径启用SockJS功能
    }

}