package com.awinson.controller;

import com.awinson.Entity.UserApi;
import com.awinson.cache.CacheManager;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.service.BitvcService;
import com.awinson.service.OkcoinService;
import com.awinson.service.UserService;
import com.awinson.valid.ApiKeyValid;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by winson on 2016/12/8.
 */
@Controller
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserApiRepository apiKeyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OkcoinService okcoinService;

    @Autowired
    private BitvcService bitvcService;

    /**
     * 用户的主页
     *
     * @return
     */
    @RequestMapping("")
    public String index() {
        return "/user/index";
    }

    /**
     * BTC交易頁面
     *
     * @return
     */
    @RequestMapping("btc-trade")
    public String trade() {
        return "/user/btc-trade";
    }

    /**
     * 设置个人参数页面
     *
     * @return
     */
    @RequestMapping("setting")
    public String setting(Model model) {
        Map<String, Object> map = new HashMap();
        Map<String, Map<String, String>> mapTmp = userService.getUserAllApi();
        map.put("apiMap", userService.getUserAllApi());
        model.addAttribute("map", map);
        return "/user/setting";
    }

    /**
     * 更新api-key
     *
     * @return
     */
    @RequestMapping("updateApiKey")
    public String updateApiKey(@Validated ApiKeyValid apiKeyValid) {
        userService.updateApiKey(apiKeyValid);
        return "forward:/u/setting";
    }

    /**
     * 获取Okcoin中国站资产信息
     * @return
     */
    @RequestMapping("getOkcoinAssets")
    @ResponseBody
    public Map<String, Object> getOkcoinAssets() {
        Map<String, Object> map = okcoinService.getSpotUserinfo(Dict.Platform.OKCOIN_CN);
        return map;
    }

    /**
     * 获取Bitvc资产信息
     * @return
     */
    @RequestMapping("getBitvcAssets")
    @ResponseBody
    public Map<String, Object> getBitvcAssets() {
        Map<String, Object> map = bitvcService.getSpotUserinfo(Dict.Platform.BITVC_CN);
        return map;
    }

    @RequestMapping("account")
    public String accoutInfo(Model model) {
        Map<String, Object> map = new HashMap();
        //获取Okcoin中国站资产信息
        Map<String, Object> okcoinCn = okcoinService.getSpotUserinfo(Dict.Platform.OKCOIN_CN);
        okcoinCn.put("platform", "Okcoin中国站");
        map.put("00",okcoinCn);

        //获取Okcoin国际站资产信息
        Map<String, Object> okcoinUn = okcoinService.getSpotUserinfo(Dict.Platform.OKCOIN_UN);
        okcoinUn.put("platform", "Okcoin国际站");
        map.put("01",okcoinUn);

        //获取Bitvc资产信息
        Map<String, Object> bitvcUn = bitvcService.getSpotUserinfo(Dict.Platform.BITVC_CN);
        bitvcUn.put("platform", "Bitvc中国站");
        map.put("10",bitvcUn);

        model.addAttribute("map", map);

        return "/user/account";
    }

    @RequestMapping("price")
    public String priceInfo(Model model){
        Map<String, Object> map = new HashMap();
        Map<String, Object> priceMap = CacheManager.getCachesByType("0");
        Map<String, Object> marginMap = CacheManager.getCachesByType("1");

        Map<String,Object> priceMapNew = new HashMap();
        for(Map.Entry<String,Object> entry : priceMap.entrySet()){
            priceMapNew.put("p"+entry.getKey(),entry.getValue());
        }

        Map<String,Object> marginMapNew = new HashMap();
        for(Map.Entry<String,Object> entry : marginMap.entrySet()){
            marginMapNew.put("m"+entry.getKey(),entry.getValue());
        }

        map.put("priceMap",priceMapNew);
        map.put("marginMap",marginMapNew);
        model.addAttribute("map", map);
        return "/user/price";
    }

}
