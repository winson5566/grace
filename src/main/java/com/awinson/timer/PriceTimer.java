package com.awinson.timer;

import com.awinson.service.PriceService;
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

    /**
     * 获取所有平台买一卖一价格
     */
    @Scheduled(cron="0/1 * *  * * ? ")   //每5秒执行一次价格查询
    public void getPrice(){
        priceService.getAllPlatformPrice();
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

}

