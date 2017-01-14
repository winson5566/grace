package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserApi;
import com.awinson.config.BitvcCnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.utils.HttpUtils;
import com.awinson.utils.MD5Util;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/16.
 */
@Service
public class BitvcServiceImpl implements BitvcService{

    private static final Logger logger = LoggerFactory.getLogger(BitvcServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BitvcCnConfig bitvcCnConfig;

    @Autowired
    private UserApiRepository userApiRepository;


    @Override
    public Map<String, Object> getSpotUserinfo(String platform) {
        Map<String,Object> map  = new HashMap();
        //获取API
        UserApi userApi1 = userService.getUserApiWithPlatformAndApiType(platform, Dict.KEY.API);
        UserApi userApi2 = userService.getUserApiWithPlatformAndApiType(platform, Dict.KEY.SECRET);
        if (userApi1!=null&&userApi2!=null){
            String accessKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            if (!StringUtil.isEmpty(accessKey)&&!StringUtil.isEmpty(secretKey)){
                map=getSpotUserinfo(platform,accessKey,secretKey);
            }
        }
        map.put("code","0");
        map.put("msg","Bitvc的key不完整，请检查！");
        return map;
    }

    @Override
    public Map<String, Object> getSpotUserinfo(String platform, String accessKey, String secretKey) {
        Map<String,Object> map  = new HashMap();
        // 构造参数签名
        Map<String, String> params = new HashMap();
        params.put("access_key", accessKey);
        params.put("created", String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
        String sign = MD5Util.buildMysignV1(params, secretKey);
        params.put("sign", sign.toLowerCase());
        // 发送post请求
        try {
            String url ;
            if (platform.equals(Dict.PLATFORM.BITVC_CN)){
                url =bitvcCnConfig.getUserinfo();
            }else {
                url =bitvcCnConfig.getUserinfo();
            }
            String result = HttpUtils.doPost(url,params);
            Gson gson = new Gson();
            Map<String,Object> resultMap = gson.fromJson(result,Map.class);
            if (resultMap.get("total")!=null&&!"".equals(resultMap.get("total").toString())){
                map.put("code", "1");
            }else {
                map.put("code", "0");
                map.put("msg", "Bitvc资产请求失败");
            }
            map.put("result", resultMap);
        } catch (IOException e) {
            logger.error("Bitvc用户资产请求失败");
            //e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> trade(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
        Map<String, Object> result = new HashMap();

        UserApi userApi1 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.KEY.API);
        UserApi userApi2 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.KEY.SECRET);
        if (userApi1 != null && userApi2 != null) {
            String apiKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            String url = null;

            // 构造参数签名
            Map<String, String> params = new HashMap();
            params.put("access_key", apiKey);
            params.put("created", String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
            if(!StringUtil.isEmpty(coin)){
                if (Dict.COIN.BTC.equals(coin)){
                    params.put("coin_type", "1");
                }else if (Dict.COIN.LTC.equals(coin)){
                    params.put("coin_type", "2");
                }
            }
            if(!StringUtil.isEmpty(amount)){
                params.put("amount", amount);
            }
            if(!StringUtil.isEmpty(price)){
                params.put("price", price);
            }
            if(!StringUtil.isEmpty(isMarketPrice)||!StringUtil.isEmpty(direction)){
                if(Dict.TRADE_TYPE.TAKER.equals(isMarketPrice)&&Dict.DIRECTION.BUY.equals(direction)){   //委托买
                    url=bitvcCnConfig.getBuy();
                }else if(Dict.TRADE_TYPE.MARKET.equals(isMarketPrice)&&Dict.DIRECTION.BUY.equals(direction)){  //市价买
                    url=bitvcCnConfig.getBuyMarket();
                }else if(Dict.TRADE_TYPE.TAKER.equals(isMarketPrice)&&Dict.DIRECTION.SELL.equals(direction)){   //委托卖
                    url=bitvcCnConfig.getSell();

                }else if(Dict.TRADE_TYPE.MARKET.equals(isMarketPrice)&&Dict.DIRECTION.SELL.equals(direction)){  //市价卖
                    url=bitvcCnConfig.getSellMarket();
                }
            }
            String sign = MD5Util.buildMysignV1(params, secretKey);
            params.put("sign", sign.toLowerCase());
            String resultHttp = HttpUtils.doPost(url,params);
            Gson gson = new Gson();
            Map<String,Object> resultMap = gson.fromJson(resultHttp,Map.class);
            if (resultMap.get("result")!=null&&"success".equals(resultMap.get("result").toString())){
                result.put("code", "1");
            }else {
                result.put("code", "0");
                result.put("msg", "Bitvc交易请求失败");
            }
            result.put("result", resultMap);
        } else {
            result.put("code", "0");
            result.put("msg", "Bitvc的key不完整，请检查！");
        }
        return result;
    }
}
