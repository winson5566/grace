package com.awinson.timer;

import com.awinson.service.PriceService;
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
//    /**
//     * 获取所有平台买一卖一价格
//     */
//    @Scheduled(cron="0/1 * *  * * ? ")   //每5秒执行一次价格查询
//    public void getPrice(){
//        priceService.getAllPlatformPrice();
//    }


    /**
     * 获取bitvc-BTC价格
     */
    @Scheduled(cron="0/1 * *  * * ? ")
    public void getBitvcBtcPrice(){
        priceService.updatePlatformPrice("10","0");
    }
    /**
     * 获取bitvc-LTC价格
     */
    @Scheduled(cron="0/1 * *  * * ? ")
    public void getBitvcLtcPrice(){
        priceService.updatePlatformPrice("10","1");
    }

    /**
     * 计算各平台价差
     */
    @Scheduled(cron = "0/1 * *  * * ? ")
    public void getPriceMargin(){
        priceService.calculationMargin();
    }

    /**
     * WebSocekt推送最新价格
     */
    @Scheduled(cron = "0/1 * *  * * ? ")
    public void pushPriceAndMargin(){
        priceService.broadcast();
    }

    /**
     * 获取所有可用用户的资产，放进缓存
     */
    @Scheduled(cron = "0/1 * *  * * ? ")
    public void getAllUserAssetsInfo2Cache(){
        userService.getAllUserAssetsInfo2Cache();
    }
}

