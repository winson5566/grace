package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserTradeSetting;

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
     * @param userId         用户id
     * @param coin           币种
     * @param doSellPlatform 执行做空的平台
     * @param doBuyPlatform  执行做多的平台
     */
    void doTrade(String userId, String coin, String doSellPlatform, String doBuyPlatform);
}
