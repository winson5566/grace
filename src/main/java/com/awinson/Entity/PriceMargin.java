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
    private String sellPlatform;
    private String buyPlatform;
    private String coin;
    private Integer deltaTime;
    private BigDecimal margin;
    private Date updateTime;

    public PriceMargin() {
    }

    public PriceMargin( String buyPlatform,String sellPlatform,String coin, Integer deltaTime, BigDecimal margin) {
        this.sellPlatform = sellPlatform;
        this.buyPlatform = buyPlatform;
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

    public String getSellPlatform() {
        return sellPlatform;
    }

    public void setSellPlatform(String sellPlatform) {
        this.sellPlatform = sellPlatform;
    }

    public String getBuyPlatform() {
        return buyPlatform;
    }

    public void setBuyPlatform(String buyPlatform) {
        this.buyPlatform = buyPlatform;
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
