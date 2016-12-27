package com.awinson.service;

import com.awinson.okcoin.WebSocketService;
import com.awinson.cache.CacheManager;
import com.awinson.Entity.PriceHistory;
import com.awinson.Entity.PriceMargin;
import com.awinson.config.*;
import com.awinson.dictionary.Dict;
import com.awinson.repository.PriceHistoryRepository;
import com.awinson.repository.PriceMarginRepository;
import com.awinson.restful.PriceController;
import com.awinson.utils.HttpUtils;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by winson on 2016/12/5.
 */
@Service
public class PriceServiceImpl implements PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);
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
    @Autowired
    private PriceMarginRepository priceMarginRepository;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private PriceController priceController;
    final private Double btcMin = 1.0;
    final private Double ltcMin = 100.0;

//    public void getAllPlatformPrice() {
//        //TODO 改线程
//        //Map<String, BigDecimal> map000 = getDepth(Dict.Platform.OKCOIN_CN, Dict.Coin.BTC);
//        //Map<String, BigDecimal> map001 = getDepth(Dict.Platform.OKCOIN_CN, Dict.Coin.LTC);
//        Map<String, BigDecimal> map100 = getDepth(Dict.Platform.BITVC_CN, Dict.Coin.BTC);
//        Map<String, BigDecimal> map101 = getDepth(Dict.Platform.BITVC_CN, Dict.Coin.LTC);
//    }
//
//    public Map<String, BigDecimal> getDepth(String platformId, String coinType) {
//        Map<String, BigDecimal> map = new HashMap();
//        String url = null;
//        Double min;
//
//        //判断平台和币种,查询对应的URL
//        if (coinType.equals(Dict.Coin.BTC)) {
//            min = btcMin;
//            if (platformId.equals(Dict.Platform.OKCOIN_CN)) {
//                url = okcoinChinaBtcConfig.getDepth();
//            } else if (platformId.equals(Dict.Platform.BITVC_CN)) {
//                url = bitvcCnBtcConfig.getDepth();
//            }
//        } else {
//            min = ltcMin;
//            if (platformId.equals(Dict.Platform.OKCOIN_CN)) {
//                url = okcoinChinaLtcConfig.getDepth();
//            } else if (platformId.equals(Dict.Platform.BITVC_CN)) {
//                url = bitvcCnLtcConfig.getDepth();
//            }
//        }
//
//        //发送GET请求,返回json文本
//        String json = null;
//        try {
//            json = HttpUtils.doGet(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (json != null && !"".equals(json)) {
//            //解析返回的json文本
//            Gson gson = new Gson();
//            Map<String, Object> jsonMap = gson.fromJson(json, Map.class);
//            ArrayList<ArrayList> asks = (ArrayList) jsonMap.get("asks");
//            if (platformId.equals(Dict.Platform.OKCOIN_CN))
//                Collections.reverse(asks);
//            ArrayList<ArrayList> bids = (ArrayList) jsonMap.get("bids");
//            BigDecimal sellPrice = getPriceByMin(asks, new BigDecimal(min));
//            BigDecimal buyPrice = getPriceByMin(bids, new BigDecimal(min));
//
//            //写入缓存
//            savePrice2Cache(platformId, coinType, sellPrice, buyPrice);
//
//            //保存如数据库
//            savePrice2DB(platformId, coinType, sellPrice, buyPrice);
//            map.put("Buy_Price", buyPrice);
//            map.put("Sell_Price", sellPrice);
//            return map;
//        }
//        return null;
//    }





    public Map<String,Object> updatePlatformPrice(String platformId,String coinType){
        Map<String, Object> map = new HashMap();
       String url = null;
        //判断平台和币种,查询对应的URL
        if (coinType.equals(Dict.Coin.BTC)) {
            if (platformId.equals(Dict.Platform.OKCOIN_CN)) {
                url = okcoinChinaBtcConfig.getTicker();
            } else if (platformId.equals(Dict.Platform.BITVC_CN)) {
                url = bitvcCnBtcConfig.getTicker();
            }
        } else {
            if (platformId.equals(Dict.Platform.OKCOIN_CN)) {
                url = okcoinChinaLtcConfig.getTicker();
            } else if (platformId.equals(Dict.Platform.BITVC_CN)) {
                url = bitvcCnLtcConfig.getTicker();
            }
        }

        //发送GET请求,返回json文本
        String json = null;
        try {
            json = HttpUtils.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json != null && !"".equals(json)) {
            //解析返回的json文本
            Gson gson = new Gson();
            Map<String, Object> jsonMap = gson.fromJson(json, Map.class);
            String time = jsonMap.get("time").toString();
            if (time.length()==10){
                time = time+"000";
            }
            Map<String,Object> ticker = (Map<String,Object> )jsonMap.get("ticker");
            String last = ticker.get("last").toString();
            String sell = ticker.get("sell").toString();
            String buy = ticker.get("buy").toString();
            BigDecimal sellPrice = new BigDecimal(sell);
            BigDecimal buyPrice = new BigDecimal(buy);
            BigDecimal lastPrice = new BigDecimal(last);

            //写入缓存
            savePrice2Cache(platformId, coinType, sellPrice, buyPrice,lastPrice,time);

            //保存如数据库
            savePrice2DB(platformId, coinType, sellPrice, buyPrice,lastPrice,time);
            map.put("Buy_Price", buyPrice);
            map.put("Sell_Price", sellPrice);
            return map;
        }
        return null;
    }

    /**
     * 比较价格,放入缓冲和数据库
     *
     * @return
     */
    public List<Map<String, Object>> calculationMargin() {
        Map<String, Object> cacheMap = CacheManager.getCachesByType("0");
        if (cacheMap != null && cacheMap.size() > 0) {
            //分类
            Map<String, Object> newMap = analyseMap(cacheMap);
            //计算价差
            List<Map<String, Object>> marginList = (List<Map<String, Object>>) subCoinMargin(newMap);
            //放入缓存
            for (Map<String, Object> m : marginList) {
                String buy_platform = m.get("buy_platform").toString();
                String sell_platform = m.get("sell_platform").toString();
                String coin = m.get("coin").toString();
                Integer deltaTime =Integer.parseInt(m.get("deltaTime").toString()) ;
                BigDecimal margin = new BigDecimal(m.get("margin").toString());
                //写入缓存
                String key = Dict.Type.margin+buy_platform+sell_platform+coin;
                CacheManager.update(key,m);
                //写入数据库
                PriceMargin priceMargin = new PriceMargin(buy_platform,sell_platform,coin, deltaTime, margin);
                priceMarginRepository.save(priceMargin);
            }
            return marginList;
        }
        return null;
    }

    /**
     * 传一个整理后的Map,分coin,计算后在合并结果list
     *
     * @param map
     * @return
     */
    private List<Map<String, Object>> subCoinMargin(Map<String, Object> map) {
        //BTC
        Map<String, Object> btcMap = (Map<String, Object>) map.get("btc");
        List<Map<String, Object>> btcList = subDirectionMargin(btcMap);
        //LTC
        Map<String, Object> ltcMap = (Map<String, Object>) map.get("ltc");
        List<Map<String, Object>> ltcList = subDirectionMargin(ltcMap);
        //合并结果
        btcList.addAll(ltcList);
        return btcList;
    }

    /**
     * 传入分coin的map,按照交易方向分类,计算后在合并结果list
     *
     * @param coinMap
     * @return
     */
    private List<Map<String, Object>> subDirectionMargin(Map<String, Object> coinMap) {
        List<Map<String, Object>> compareList = new ArrayList();
        List<Map<String, Object>> sellList = (List<Map<String, Object>>) coinMap.get("sell");
        List<Map<String, Object>> buyList = (List<Map<String, Object>>) coinMap.get("buy");
        for (Map<String, Object> sellMap : sellList) {
            for (Map<String, Object> buyMap : buyList) {
                if (!sellMap.get("platform").toString().equals(buyMap.get("platform").toString())) {
                    Map<String, Object> compareMap = new HashMap();
                    BigDecimal buyPrice = new BigDecimal(Double.parseDouble(buyMap.get("price").toString()));
                    BigDecimal sellPrice = new BigDecimal(Double.parseDouble(sellMap.get("price").toString()));
                    BigInteger buyTime = new BigInteger(buyMap.get("timestamp").toString());
                    BigInteger sellTime = new BigInteger(sellMap.get("timestamp").toString());

                    compareMap.put("buy_platform",buyMap.get("platform"));
                    compareMap.put("sell_platform",sellMap.get("platform"));
                    compareMap.put("deltaTime", sellTime.subtract(buyTime).abs());
                    compareMap.put("margin", buyPrice.subtract(sellPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
                    compareMap.put("coin", sellMap.get("coin"));
                    compareList.add(compareMap);
                }
            }
        }
        logger.debug(String.valueOf( compareList.size()));
        return compareList;
    }

    /**
     * 整理map,按币种和方向
     *
     * @param cacheMap
     * @return
     */
    private Map<String, Object> analyseMap(Map<String, Object> cacheMap) {
        List<Map<String, Object>> btcList = new ArrayList();
        List<Map<String, Object>> ltcList = new ArrayList();
        for (Map.Entry<String, Object> datas : cacheMap.entrySet()) {
            if (datas.getKey().matches("^0\\d{4}")&&!datas.getKey().endsWith("2")) {
                Map<String, Object> entity = (Map<String, Object>) datas.getValue();
                String coin = entity.get("coin").toString();
                if (Dict.Coin.BTC.equals(coin)) {
                    btcList.add(entity);
                } else if (Dict.Coin.LTC.equals(coin)) {
                    ltcList.add(entity);
                }
            }
        }

        Map<String, Object> result = new HashMap();
        result.put("btc", direction2map(btcList));
        result.put("ltc", direction2map(ltcList));
        return result;
    }

    /**
     * 按交易方向分类
     *
     * @param list
     * @return
     */
    private Map<String, Object> direction2map(List<Map<String, Object>> list) {
        List<Map<String, Object>> sellList = new ArrayList();
        List<Map<String, Object>> buyList = new ArrayList();
        for (Map<String, Object> map : list) {
            String direction = map.get("direction").toString();
            if (Dict.direction.sell.equals(direction)) {
                sellList.add(map);
            } else if (Dict.direction.buy.equals(direction)) {
                buyList.add(map);
            }
        }
        Map<String, Object> result = new HashMap();
        result.put("sell", sellList);
        result.put("buy", buyList);
        return result;
    }


    /**
     * 保存买一卖一到缓存
     *
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    public void savePrice2Cache(String platformId, String coinType, BigDecimal sellPrice, BigDecimal buyPrice,BigDecimal lastPrice,String timestamp) {


        Map<String, Object> sellMap = new HashMap();
        sellMap.put("platform", platformId);
        sellMap.put("coin", coinType);
        sellMap.put("direction", Dict.direction.sell);
        sellMap.put("price", sellPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        sellMap.put("timestamp", timestamp);
        CacheManager.update(Dict.Type.price + platformId + coinType + Dict.direction.sell, sellMap);

        Map<String, Object> buyMap = new HashMap();
        buyMap.put("platform", platformId);
        buyMap.put("coin", coinType);
        buyMap.put("direction", Dict.direction.buy);
        buyMap.put("price", buyPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        buyMap.put("timestamp", timestamp);
        CacheManager.update(Dict.Type.price + platformId + coinType + Dict.direction.buy, buyMap);

        Map<String, Object> lastMap = new HashMap();
        lastMap.put("platform", platformId);
        lastMap.put("coin", coinType);
        lastMap.put("direction", Dict.direction.last);
        lastMap.put("price", lastPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        lastMap.put("timestamp", timestamp);
        CacheManager.update(Dict.Type.price + platformId + coinType + Dict.direction.last, lastMap);

    }

    /**
     * 保存买一卖一到数据库
     *
     * @param platformId
     * @param coinType
     * @param sellPrice
     * @param buyPrice
     */
    public void savePrice2DB(String platformId, String coinType, BigDecimal sellPrice, BigDecimal buyPrice,BigDecimal lastPrice,String timestamp) {
        PriceHistory entity = new PriceHistory(platformId, coinType, sellPrice, buyPrice,lastPrice,timestamp);
       //priceHistoryRepository.save(entity);
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
            BigDecimal price = new BigDecimal((Double) list.get(0));
            BigDecimal amount = new BigDecimal((Double) list.get(1));
            //计算满足最小设定量的平均价格
            if (minAmount.compareTo(amountTotal.add(amount)) < 0) {
                priceTotal = priceTotal.add(price.multiply(minAmount.subtract(amountTotal)));
                return priceTotal.divide(minAmount);
            } else {
                amountTotal = amountTotal.add(amount);
                priceTotal = priceTotal.add(price.multiply(amount));
            }

        }
        return null;
    }

    @Override
    public void broadcast() {
        String price = priceController.getPrice();
        String margin = priceController.getPriceMargin();
        if (!StringUtil.isEmpty(price))
            webSocketService.broadcast("price",price);
        if (!StringUtil.isEmpty(margin))
            webSocketService.broadcast("margin",margin);
    }
}

