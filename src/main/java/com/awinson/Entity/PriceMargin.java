package com.awinson.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by winson on 2016/12/7.
 */
@Entity
@Table(name="price_margin")
public class PriceMargin {

    private String id;
    private String highPlatform;
    private String highDirection;
    private String lowPlatform;
    private String lowDirection;
    private String coin;
    private Integer deltaTime;
    private BigDecimal margin;
    private Date updateTime;

    public PriceMargin() {
    }

    public PriceMargin(String highPlatform, String highDirection, String lowPlatform, String lowDirection, String coin, Integer deltaTime, BigDecimal margin) {
        this.highPlatform = highPlatform;
        this.highDirection = highDirection;
        this.lowPlatform = lowPlatform;
        this.lowDirection = lowDirection;
        this.coin = coin;
        this.deltaTime = deltaTime;
        this.margin = margin;
        this.id= UUID.randomUUID().toString();
        this.updateTime=new Date();
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHighPlatform() {
        return highPlatform;
    }

    public void setHighPlatform(String highPlatform) {
        this.highPlatform = highPlatform;
    }

    public String getHighDirection() {
        return highDirection;
    }

    public void setHighDirection(String highDirection) {
        this.highDirection = highDirection;
    }

    public String getLowPlatform() {
        return lowPlatform;
    }

    public void setLowPlatform(String lowPlatform) {
        this.lowPlatform = lowPlatform;
    }

    public String getLowDirection() {
        return lowDirection;
    }

    public void setLowDirection(String lowDirection) {
        this.lowDirection = lowDirection;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public Integer getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(Integer deltaTime) {
        this.deltaTime = deltaTime;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
