package com.awinson.controller;

import com.awinson.Entity.UserApi;
import com.awinson.Entity.UserLog;
import com.awinson.Entity.UserTradeSetting;
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
    private UserService userService;

    /**
     * 用户的主页
     */
    @RequestMapping("")
    public String index() {
        return "user/trade";
    }

    /**
     * 交易頁面
     */
    @RequestMapping("trade")
    public String trade() {
        return "user/trade";
    }

    /**
     * 设置个人参数页面
     */
    @RequestMapping("setting")
    public String setting(Model model) {
        Map<String, Object> map = new HashMap();
        Map<String, Map<String, String>> mapTmp = userService.getUserAllApi();
        map.put("apiMap", userService.getUserAllApi());
        model.addAttribute("map", map);
        return "user/setting";
    }

    /**
     * 更新api-key
     */
    @RequestMapping("updateApiKey")
    public String updateApiKey(@Validated ApiKeyValid apiKeyValid) {
        userService.updateApiKey(apiKeyValid);
        return "forward:u/setting";
    }


    /**
     * 获取当前用户的设置
     *
     * @return
     */
    @RequestMapping("getUserTradeSetting")
    @ResponseBody
    public String getUserTradeSetting() {
        UserTradeSetting userTradeSetting = userService.getUserTradeSetting();
        Gson gson = new Gson();
        return gson.toJson(userTradeSetting);
    }

    /**
     * 更新用户交易设置
     *
     * @param buyPlatform
     * @param sellPlatform
     * @param coin
     * @param margin
     * @return
     */
    @RequestMapping("updateUserTradeSetting")
    @ResponseBody
    public String updateUserTradeSetting(String buyPlatform, String sellPlatform, String coin, String margin) {
        return userService.updateUserTradeSetting(buyPlatform, sellPlatform, coin, margin);
    }

    /**
     * 更新用户交易设置(自动交易和阀值)
     *
     * @param autoTradeBtc
     * @param autoTradeLtc
     * @return
     */
    @RequestMapping("updateUserTradeSettingAuto")
    @ResponseBody
    public String updateUserTradeSettingAuto(String autoTradeBtc, String autoTradeLtc, String autoThresholdBtc, String autoThresholdLtc) {
        return userService.updateUserTradeSettingAuto(autoTradeBtc, autoTradeLtc, autoThresholdBtc, autoThresholdLtc);
    }

    /**
     * 更新用户交易设置(最小交易量)
     *
     * @param eachAmountBtc
     * @param eachAmountLtc
     * @return
     */
    @RequestMapping("updateUserTradeSettingEachAmount")
    @ResponseBody
    public String updateUserTradeSettingEachAmount(String eachAmountBtc, String eachAmountLtc) {
        return userService.updateUserTradeSettingEachAmount(eachAmountBtc, eachAmountLtc);
    }

}
