package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserApi;
import com.awinson.config.OkcoinCnConfig;
import com.awinson.config.OkcoinUnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.utils.HttpUtils;
import com.awinson.utils.MD5Util;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
@Service
@Transactional
public class OkcoinServiceImpl implements OkcoinService {

    private static final Logger logger = LoggerFactory.getLogger(OkcoinServiceImpl.class);

    @Autowired
    private OkcoinCnConfig okcoinCnConfig;
    @Autowired
    private OkcoinUnConfig okcoinUnConfig;
    @Autowired
    private UserService userService;
@Autowired
private UserApiRepository userApiRepository;
    @Override
    public Map<String, Object> getSpotUserinfo(String platform) {
        Map<String, Object> map = new HashMap();
        //获取API
        UserApi userApi1 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.api);
        UserApi userApi2 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.secret);
        if (userApi1 != null && userApi2 != null) {
            String apiKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            return getSpotUserinfo(platform, apiKey, secretKey);
        }
        map.put("code", "0");
        map.put("msg", "Okcoind的key不完整，请检查！");
        return map;
    }

    @Override
    public Map<String, Object> getSpotUserinfo(String platform, String apiKey, String secretKey) {
        Map<String, Object> map = new HashMap();
        if (!StringUtil.isEmpty(apiKey) && !StringUtil.isEmpty(secretKey)) {
            // 构造参数签名
            Map<String, String> params = new HashMap();
            params.put("api_key", apiKey);
            String sign = MD5Util.buildMysignV1(params, secretKey);
            params.put("sign", sign);
            // 发送post请求
            try {
                String url;
                if (platform.equals(Dict.Platform.OKCOIN_CN)) {
                    url = okcoinCnConfig.getUserinfo();
                } else {
                    url = okcoinUnConfig.getUserinfo();
                }
                String result = HttpUtils.doPost(url, params);
                map.put("code", "1");
                map.put("msg", "OK");
                Gson gson = new Gson();
                map.put("result", gson.fromJson(result, Map.class));
            } catch (IOException e) {
                logger.error("Okcoin用户资产请求失败");
                //e.printStackTrace();
            }
            return map;
        }
        map.put("code", "0");
        map.put("msg", "Okcoind的key不完整，请检查！");
        return map;
    }

    @Override
    public Map<String, Object> trade(User user,String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
        Map<String, Object> result = new HashMap();

        UserApi userApi1 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.key.api);
        UserApi userApi2 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.key.secret);
        if (userApi1 != null && userApi2 != null) {
            String apiKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            String url = okcoinCnConfig.getTrade();

            // 构造参数签名
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", apiKey);
            if(!StringUtil.isEmpty(coin)){
                if (Dict.Coin.BTC.equals(coin)){
                    params.put("symbol", "btc_cny");
                }else if (Dict.Coin.LTC.equals(coin)){
                    params.put("symbol", "ltc_cny");
                }
            }
            if(!StringUtil.isEmpty(isMarketPrice)||!StringUtil.isEmpty(direction)){
                if(Dict.TradeType.TAKER.equals(isMarketPrice)&&Dict.direction.buy.equals(direction)){   //委托买
                    params.put("type", "buy");
                }else if(Dict.TradeType.MARKET.equals(isMarketPrice)&&Dict.direction.buy.equals(direction)){  //市价买
                    params.put("type", "buy_market");
                }else if(Dict.TradeType.TAKER.equals(isMarketPrice)&&Dict.direction.sell.equals(direction)){   //委托买
                    params.put("type", "sell");
                }else if(Dict.TradeType.MARKET.equals(isMarketPrice)&&Dict.direction.sell.equals(direction)){  //市价买
                    params.put("type", "sell_market");
                }
            }

            if(!StringUtil.isEmpty(price)){
                params.put("price", price);
            }
            if(!StringUtil.isEmpty(amount)){
                params.put("amount", amount);
            }
            String sign = MD5Util.buildMysignV1(params,secretKey);
            params.put("sign", sign);

            // 发送post请求
            String resultMsg = HttpUtils.doPost(url, params);
            result.put("code", "1");
            result.put("result", resultMsg);
        } else {
            result.put("code", "0");
            result.put("msg", "Okcoind的key不完整，请检查！");
        }
        return result;
    }

    @Override
    public   Map<String, Object> order_info(User user,String platform, String coin, String orderId) throws HttpException, IOException {
        Map<String, Object> result = new HashMap();
        UserApi userApi1 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.key.api);
        UserApi userApi2 =  userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(),platform,Dict.key.secret);
        if (userApi1 != null && userApi2 != null) {
            String apiKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            String url = okcoinCnConfig.getOrderInfo();
            // 构造参数签名
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", apiKey);
            if(!StringUtil.isEmpty(coin)){
                if (Dict.Coin.BTC.equals(coin)){
                    params.put("symbol", "btc_cny");
                }else if (Dict.Coin.LTC.equals(coin)){
                    params.put("symbol", "ltc_cny");
                }
            }
            if(!StringUtil.isEmpty(orderId)){
                params.put("order_id", orderId);
            }

            String sign = MD5Util.buildMysignV1(params, secretKey);
            params.put("sign", sign);

            // 发送post请求
            String resultMsg = HttpUtils.doPost(url, params);

            result.put("code", "1");
            result.put("result", resultMsg);
        } else {
            result.put("code", "0");
            result.put("msg", "Okcoind的key不完整，请检查！");
        }
        return result;
    }
}