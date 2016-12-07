package com.awinson.controller;

import com.awinson.config.CacheManager;
import com.awinson.service.PriceService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by winson on 2016/12/7.
 */
@Controller
public class PriceController {
    @Autowired
    private PriceService priceService;

    /**
     * 查找最新价格(code)
     * @return
     */
    @RequestMapping("price_code")
    @ResponseBody
    public String getPriceCode() {
        Map<String, Object> result = new HashMap();
        Map<String, Object> map = CacheManager.getCachesByType("0");
        if (map!=null&&map.size()>0){
            result.put("code",1);
            result.put("msg","价格获取成功");
            result.put("datas",map);
            Gson gson = new Gson();
            return gson.toJson(result);
        }else {
            result.put("code",0);
            result.put("msg","缓存中没有数据,请稍后重试");
            Gson gson = new Gson();
            return gson.toJson(result);
        }
    }

    /**
     * 查找最新价格
     * @return
     */
    @RequestMapping("price")
    @ResponseBody
    public String getPrice() {
        Map<String, Object> result = new HashMap();
        Map<String, Object> map = CacheManager.getCachesByType("0");
        if (map!=null&&map.size()>0){
            result.put("code",1);
            result.put("msg","价格获取成功");
            result.put("datas",map);
        }else {
            result.put("code",0);
            result.put("msg","缓存中没有数据,请稍后重试");
        }
        Gson gson = new Gson();
        return gson.toJson(result);
    }



}
