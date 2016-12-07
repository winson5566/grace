package com.awinson.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by winson on 2016/12/5.
 */
public interface PriceService {

    /**
     * 获取所有平台的买一卖一价格,放入缓存并写入数据库
     */
    void getAllPlatformPrice();

    /**
     * 获取市场深度
     * @param platformId
     * @param coinType
     * @return
     */
    Map<String,BigDecimal>  getDepth(String platformId, String coinType);


    /**
     * 从缓存中获取价格,对比,讲价差写入缓冲并出入数据库
     */
    List<Map<String,Object>> calculationMargin();
}
