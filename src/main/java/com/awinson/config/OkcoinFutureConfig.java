package com.awinson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 10228 on 2017/1/14.
 */
@Component
@ConfigurationProperties(prefix = "okcoin.future",locations = "classpath:api.yml")
public class OkcoinFutureConfig {
    private String userinfo;
    private String position;

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
