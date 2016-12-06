package com.awinson.service;

import com.awinson.Entity.PriceHistory;
import com.awinson.config.*;
import com.awinson.repository.PriceHistoryRepository;
import com.awinson.utils.HttpUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winson on 2016/12/5.
 */
@Service
public class PriceServiceImpl implements PriceService {

    private static final Logger logger =  LoggerFactory.getLogger(PriceServiceImpl.class);
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private OkcoinCnBtcConfig okcoinChinaBtcConfig;
    @Autowired
    private OkcoinCnLtcConfig okcoinChinaLtcConfig;
    @Autowired
    private BitvcCnBtcConfig bitvcCnBtcConfig;
    @Autowired
    private BitvcCnLtcConfig bitvcCnLtcConfig;
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;
    final private Double btcMin = 1.0;
    final private Double ltcMin = 100.0;

    public void getAllPlatformPrice() {
        //TODO 改线程
        Map<String,BigDecimal> map000 = getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.BTC);
        Map<String,BigDecimal> map001 = getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.LTC);
        Map<String,BigDecimal> map100 = getDepth(Dict.Platform.BITVC_CN,Dict.Coin.BTC);
        Map<String,BigDecimal> map101 = getDepth(Dict.Platform.BITVC_CN,Dict.Coin.LTC);
    }

    public Map<String,BigDecimal> getDepth(int platformId, int coinType) {
        Map<String,BigDecimal>  map = new HashMap();
        String url =null ;
        Double min;

        //判断平台和币种,查询对应的URL
        if (coinType == Dict.Coin.BTC) {
            min = btcMin;
            if (platformId == Dict.Platform.OKCOIN_CN) {
                url = okcoinChinaBtcConfig.getDepth();
            } else if (platformId == Dict.Platform.BITVC_CN) {
                url = bitvcCnBtcConfig.getDepth();
            }
        } else {
            min = ltcMin;
            if (platformId == Dict.Platform.OKCOIN_CN) {
                url = okcoinChinaLtcConfig.getDepth();
            } else if (platformId == Dict.Platform.BITVC_CN) {
                url = bitvcCnLtcConfig.getDepth();
            }
        }

        //发送GET请求,返回json文本
        String json = null;
        try {
            json = httpUtils.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析返回的json文本
        Gson gson = new Gson();
        Map<String, Object> jsonMap = gson.fromJson(json, Map.class);
        ArrayList<ArrayList> asks = (ArrayList) jsonMap.get("asks");
        if (platformId==Dict.Platform.OKCOIN_CN)
            Collections.reverse(asks);
        ArrayList<ArrayList> bids = (ArrayList) jsonMap.get("bids");
        BigDecimal sellPrice = getPriceByMin(asks, new BigDecimal(min));
        BigDecimal buyPrice = getPriceByMin(bids, new BigDecimal(min));

        //写入缓存

        //保存如数据库
        savePrice2DB(platformId,coinType,sellPrice,buyPrice);
        map.put("Sell_Price",sellPrice);
        map.put("Buy_Price",buyPrice);
        return map;

    }

    /**
     * 保存买一卖一到缓存
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    private void savePrice2Cache(int platformId, int coinType,Double sellPrice,Double buyPrice){

    }

    /**
     * 保存买一卖一到数据库
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    private void savePrice2DB(int platformId, int coinType,BigDecimal sellPrice,BigDecimal buyPrice){
        PriceHistory entity =  new PriceHistory(platformId,coinType,sellPrice,buyPrice);
        priceHistoryRepository.save(entity);
    }


    /**
     * 获取指定累计最小数量的买一卖一价格(用于确保买一卖一价格的有效性)
     *
     * @param lists
     * @return
     */
    private BigDecimal getPriceByMin(ArrayList<ArrayList> lists, BigDecimal minAmount) {
        BigDecimal amountTotal = new BigDecimal(0.00000);
        BigDecimal priceTotal = new BigDecimal(0.00000);
        //遍历[[5277.96,0.042],[5277.55,0.039],[5277.5,2]......
        for (ArrayList list : lists) {
            //[5277.96,0.042]
            BigDecimal price = new BigDecimal((Double)list.get(0));
            BigDecimal amount = new BigDecimal((Double) list.get(1));
            //计算满足最小设定量的平均价格
            if (minAmount.compareTo(amountTotal.add(amount))<0) {
                priceTotal = priceTotal.add(price.multiply(minAmount.subtract(amountTotal)));
                return priceTotal.divide(minAmount);
            } else {
                amountTotal = amountTotal.add(amount);
                priceTotal = priceTotal.add(price.multiply(amount));
            }

        }
        return null;
    }
}

