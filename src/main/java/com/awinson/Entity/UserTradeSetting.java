package com.awinson.Entity;

import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 10228 on 2016/12/26.
 */
@Entity
@Table(name = "user_trade_setting")
public class UserTradeSetting {
    private String id;
    private String userId;
    private String autoTrade;
    private String autoTradeBtc;
    private String autoTradeLtc;
    private String marginJson;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAutoTrade() {
        return autoTrade;
    }

    public void setAutoTrade(String autoTrade) {
        this.autoTrade = autoTrade;
    }

    public String getAutoTradeBtc() {
        return autoTradeBtc;
    }

    public void setAutoTradeBtc(String autoTradeBtc) {
        this.autoTradeBtc = autoTradeBtc;
    }

    public String getAutoTradeLtc() {
        return autoTradeLtc;
    }

    public void setAutoTradeLtc(String autoTradeLtc) {
        this.autoTradeLtc = autoTradeLtc;
    }

    public String getMarginJson() {
        return marginJson;
    }

    public void setMarginJson(String marginJson) {
        this.marginJson = marginJson;
    }
}
