package com.awinson.restful;

import com.awinson.cache.CacheManager;
import com.awinson.service.PriceService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by winson on 2016/12/7.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private PriceService priceService;
    /**
     * 获取所有缓存的测试url
     * @return
     */
    @RequestMapping("getAllCache")
    public String getAllCache(){
        Map<String,Object> map =  CacheManager.getCaches();
        if (map!=null&&map.size()>0){
            Gson gson = new Gson();
            return gson.toJson(map);
        }
        return "cache is empty!";
    }

    /**
     * 获取指定类型缓存的测试url
     * @return
     */
    @RequestMapping("getAllCacheByType")
    public String getAllCacheByType(String type){
        Map<String,Object> map =  CacheManager.getCachesByType(type);
        if (map!=null&&map.size()>0){
            Gson gson = new Gson();
            return gson.toJson(map);
        }
        return "cache in type="+type+" is empty!";
    }

    /**
     * 根据缓存中的价格计算margin的测试
     * @return
     */
    @RequestMapping("test")
    public String test(){
        List<Map<String, Object>> marginList = priceService.calculationMargin();
        Gson gson = new Gson();
        return gson.toJson(marginList).toString();
    }
}
