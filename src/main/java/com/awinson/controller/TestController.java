package com.awinson.controller;

import com.awinson.Entity.PriceHistory;
import com.awinson.repository.PriceHistoryRepository;
import com.awinson.service.PriceService;
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
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private PriceService priceService;

    @RequestMapping("test")
    @ResponseBody
    public void test(){

    }

    @RequestMapping("test1")
    @ResponseBody
    public void test1(){
        priceService.getDepth(0,0);
    }
}
