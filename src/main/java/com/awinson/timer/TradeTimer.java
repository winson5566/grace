package com.awinson.timer;

import com.awinson.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by 10228 on 2016/12/27.
 */
@Component
@EnableScheduling
public class TradeTimer {
    @Autowired
    private TradeService tradeService;

    /**
     * 自动交易流程
     */
    @Scheduled(initialDelay=8000, fixedDelay=1000)
    public void autoTrade() {
        tradeService.tradeStart();
    }
}
