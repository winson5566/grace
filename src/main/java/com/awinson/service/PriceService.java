package com.awinson.service;

import javafx.beans.binding.ObjectExpression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by winson on 2016/12/5.
 */
public interface PriceService {

//    /**
//     * 获取所有平台的买一卖一价格,放入缓存并写入数据库
//     */
//    void getAllPlatformPrice();

//    /**
//     * 获取市场深度
//     *
//     * @param platformId
//     * @param coinType
//     * @return
//     */
//    Map<String, BigDecimal> getDepth(String platformId, String coinType);

    /**
     * Rest方式获取制定平台价格
     * @param platformId
     * @param coinType
     */
    Map<String,Object>  updatePlatformPrice(String platformId,String coinType);

    /**
     * 从缓存中获取价格,对比,讲价差写入缓冲并出入数据库
     *
     * @return
     */
    List<Map<String, Object>> calculationMargin();

    /**
     * WebSocket推送价格和价差
     */
    void pushPriceAndMargin();

    /**
     * 保存买一卖一到缓存
     *
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    void savePrice2Cache(String platformId, String coinType, BigDecimal sellPrice, BigDecimal buyPrice,BigDecimal lastPrice,String timestamp);

    /**
     * 保存买一卖一到数据库
     *
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    void savePrice2DB(String platformId, String coinType, BigDecimal sellPrice, BigDecimal buyPrice,BigDecimal lastPrice,String timestamp);
}
