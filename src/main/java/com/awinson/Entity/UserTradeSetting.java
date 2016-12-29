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
    private String autoThresholdBtc;
    private String autoThresholdLtc;
    private String autoTradeBtc;
    private String autoTradeLtc;
    private String eachAmountBtc;
    private String eachAmountLtc;
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

    public String getAutoThresholdBtc() {
        return autoThresholdBtc;
    }

    public void setAutoThresholdBtc(String autoThresholdBtc) {
        this.autoThresholdBtc = autoThresholdBtc;
    }

    public String getAutoThresholdLtc() {
        return autoThresholdLtc;
    }

    public void setAutoThresholdLtc(String autoThresholdLtc) {
        this.autoThresholdLtc = autoThresholdLtc;
    }

    public String getEachAmountBtc() {
        return eachAmountBtc;
    }

    public void setEachAmountBtc(String eachAmountBtc) {
        this.eachAmountBtc = eachAmountBtc;
    }

    public String getEachAmountLtc() {
        return eachAmountLtc;
    }

    public void setEachAmountLtc(String eachAmountLtc) {
        this.eachAmountLtc = eachAmountLtc;
    }
}
