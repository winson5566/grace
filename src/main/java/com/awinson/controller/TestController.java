package com.awinson.controller;

import com.awinson.Entity.PriceHistory;
import com.awinson.mapper.PriceHistoryMapper;
import com.awinson.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * Created by winson on 2016/12/2.
 */
@Controller
public class TestController {

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @RequestMapping("test")
    @ResponseBody
    public String test(){
        PriceHistory priceHistory =  priceHistoryMapper.findPriceHistoryByType(1);
        PriceHistory priceHistory1 = priceHistoryRepository.findByType(1);
        for (int i=0;i<100;i++){
            PriceHistory priceHistory2 = new PriceHistory();
            priceHistory2.setId(UUID.randomUUID().toString());
            priceHistory2.setBuy("222");
            priceHistory2.setSell("333");
            priceHistory2.setType(1);
            priceHistory2.setUpdateTime(new Date());
            priceHistoryRepository.save(priceHistory2);
        }


        return priceHistory.getBuy();

    }
}
