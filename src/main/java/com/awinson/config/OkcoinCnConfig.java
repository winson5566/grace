package com.awinson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@ConfigurationProperties(prefix = "okcoin.cn",locations = "classpath:api.yml")
public class OkcoinCnConfig {
    private String userinfo;

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }
}
