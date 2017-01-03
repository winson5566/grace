package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserTradeSetting;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/27.
 */
public interface TradeService {
    /**
     * 定时自定交易的入口
     */
    void tradeStart();

    /**
     * 单个可用用户的自动交易流程
     *
     * @param user
     */
    void oneUserTrade(User user);

    /**
     * 判断是否会触发
     * @param user
     * @param userTradeSetting
     * @param coin
     */
    void judgeTrigger(User user, UserTradeSetting userTradeSetting, String coin);

    /**
     * 已根据策略触发用户的对冲交易
     *
     * @param user         用户
     * @param coin           币种
     * @param doSellPlatform 执行做空的平台
     * @param doBuyPlatform  执行做多的平台
     */
    void doTrade(User user, String coin, String doSellPlatform, String doBuyPlatform);

    /**
     * 全自动的现货交易接口（现支持okcoin和bitvc）
     * @param islippage 0市价 1滑价
     * @param user
     * @param platform
     * @param coin
     * @param direction
     * @param amount
     * @return
     * @throws IOException
     */
    Map<String,Object> trade(String islippage,User user,String platform,String coin,String direction,String amount) throws IOException;


    /**
     * 通用的现货交易接口（现支持okcoin和bitvc）
     * 修正okcoin的bitvc的市价买入用的CNY数量，统一使用amount
     * @param user  用户
     * @param platform  平台
     * @param coin  币种
     * @param direction  方向
     * @param isMarketPrice 是否是市价 0:委托价 1:市价
     * @param amount 数量
     * @param price 委托价格（如果是市价购买，可为空）
     * @return
     */
    Map<String,Object> tradeCommon(User user,String platform,String coin,String direction,String isMarketPrice,String amount,String price) throws IOException;

    /**
     * 现货交易接口（现支持okcoin和bitvc）
     * 注意：这个接口并不通用，例如okcoin的bitvc的市价买入用的CNY数量，okcoin使用price，bitvc是用amount
     * @param user  用户
     * @param platform  平台
     * @param coin  币种
     * @param direction  方向
     * @param isMarketPrice 是否是市价 0:委托价 1:市价
     * @param amount 数量
     * @param price 委托价格（如果是市价购买，可为空）
     * @return
     */
    Map<String,Object> tradeBase(User user,String platform,String coin,String direction,String isMarketPrice,String amount,String price) throws IOException;
}
