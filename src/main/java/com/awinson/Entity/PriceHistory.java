package com.awinson.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by winson on 2016/12/2.
 */
@Entity
@Table(name="price_history")
public class PriceHistory {
    private String id;
    private String platform;
    private String coin;
    private BigDecimal sellPrice;
    private BigDecimal buyPrice;
    private BigDecimal lastPrice;
    private String timestamp;

    public PriceHistory() {
    }

    public PriceHistory(String platform, String coin, BigDecimal sellPrice,BigDecimal buyPrice, BigDecimal lastPrice,String timestamp) {
        this.platform = platform;
        this.coin = coin;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.lastPrice = lastPrice;
        this.id= UUID.randomUUID().toString();
        this.timestamp=timestamp;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }
}

