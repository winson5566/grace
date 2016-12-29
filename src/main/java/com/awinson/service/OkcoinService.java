package com.awinson.service;

import com.awinson.Entity.User;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
public interface OkcoinService {

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 00 中国站    01国际站
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform);

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 00 中国站    01国际站
     * @param platform
     * @param apiKey
     * @param secretKey
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform,String apiKey,String secretKey);

    /**
     * 通用的现货交易接口
     * @param user  用户对象
     * @param platform  平台
     * @param coin  币种
     * @param direction  方向
     * @param isMarketPrice 是否是市价 0:委托价 1:市价
     * @param amount 数量（市价买单不传）(现价单：BTC 数量大于等于0.01 / LTC 数量大于等于0.1)
     * @param price 委托价格（市价卖单不传）（市价买单这里为cny的数量,BTC :最少买入0.01个BTC 的金额(金额>0.01*卖一价),LTC :最少买入0.1个LTC 的金额(金额>0.1*卖一价)])
     * @return
     */
    Map<String,Object> trade(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException;

    /**
     *查询订单信息
     * @param user  用户对象
     * @param platform  平台
     * @param coin   币种
     * @param orderId 订单id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    Map<String,Object> order_info(User user,String platform,  String coin, String orderId) throws HttpException, IOException;
}
