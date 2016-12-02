package com.awinson.controller;

import com.awinson.Entity.PriceHistory;
import com.awinson.mapper.PriceHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by winson on 2016/12/2.
 */
@Controller
public class TestController {

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    @RequestMapping("test")
    @ResponseBody
    public String test(){
        PriceHistory priceHistory =  priceHistoryMapper.findPriceHistoryByType(1);
        return priceHistory.getBuy();

    }
}
