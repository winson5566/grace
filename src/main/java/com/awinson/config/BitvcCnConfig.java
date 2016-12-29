package com.awinson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@ConfigurationProperties(prefix = "bitvc.cn",locations = "classpath:api.yml")
public class BitvcCnConfig {
    private String userinfo;
    private String buyMarket;
    private String buy;
    private String sellMarket;
    private String sell;
    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getBuyMarket() {
        return buyMarket;
    }

    public void setBuyMarket(String buyMarket) {
        this.buyMarket = buyMarket;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSellMarket() {
        return sellMarket;
    }

    public void setSellMarket(String sellMarket) {
        this.sellMarket = sellMarket;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }
}
