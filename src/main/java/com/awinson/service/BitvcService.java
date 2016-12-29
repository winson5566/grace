package com.awinson.service;

import com.awinson.Entity.User;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
public interface BitvcService {

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 10 中国站    11国际站
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform);

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 10 中国站    11国际站
     * @param platform
     * @param apiKey
     * @param secretKey
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform,String accessKey,String secretKey);

    /**
     * 通用的现货交易接口
     * @param user  用户对象
     * @param platform  平台
     * @param coin  币种
     * @param direction  方向
     * @param isMarketPrice 是否是市价 0:委托价 1:市价
     * @param amount 数量 (如果是市价卖，这里是卖B的数量，如果是市价买，这里是CNY的数量)
     * @param price 委托价格（市价不传）
     * @return
     */
    Map<String,Object> trade(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException;
}
