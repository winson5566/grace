package com.awinson.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@ConfigurationProperties(prefix = "okcoin.cn.btc",locations = "classpath:api.yml")
public class OkcoinCnBtcConfig {

    private String ticker;
    private String depth;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }
}
