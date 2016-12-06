package com.awinson.timer;

import com.awinson.config.Dict;
import com.awinson.service.PriceService;
import javafx.scene.layout.BackgroundImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by winson on 2016/12/5.
 */
@Component
@EnableScheduling
public class TestTimer {


    @Autowired
    private PriceService priceService;

    /**
     * 获取所有平台买一卖一价格
     */
    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次价格查询
    public void getPrice(){
        priceService.getAllPlatformPrice();
    }


}

