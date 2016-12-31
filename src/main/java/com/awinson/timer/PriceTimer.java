package com.awinson.timer;

import com.awinson.service.PriceService;
import com.awinson.service.TradeService;
import com.awinson.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@EnableScheduling
public class PriceTimer {

    @Autowired
    private PriceService priceService;
    @Autowired
    private UserService userService;

    /**
     * 获取bitvc-BTC价格
     */
//    @Scheduled(cron = "0/1 * *  * * ? ")
    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void getBitvcBtcPrice() {
        priceService.updatePlatformPrice("10", "0");
    }

    /**
     * 获取bitvc-LTC价格
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void getBitvcLtcPrice() {
        priceService.updatePlatformPrice("10", "1");
    }

    /**
     * 计算各平台价差
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 500)
    public void getPriceMargin() {
        priceService.calculationMargin();
    }

    /**
     * WebSocekt推送最新价格
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 1000)
    public void pushPriceAndMargin() {
        priceService.pushPriceAndMargin();
    }

    /**
     * 获取所有可用用户的资产，放进缓存
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 1000)
    public void getAllUserAssetsInfo2Cache() {
        userService.getAllUserAssetsInfo2Cache();
    }

    /**
     * 推送所有用户的资产信息
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 1000)
    public void pushAccoutInfoByWebSocket() {
        userService.pushAccoutInfoByWebSocket();
    }

    /**
     * 推送所有用户的日志
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 2000)
    public void pushUserLogByWebSocke() {
        userService.pushUserLogByWebSocket();
    }

    /**
     * 推送所有用户的设置
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 2000)
    public void pushUserSettingWebSocke() {
        userService.pushUserSetting();
    }

}

