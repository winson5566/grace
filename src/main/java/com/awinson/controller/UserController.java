package com.awinson.controller;

import com.awinson.Entity.UserApi;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.service.OkcoinService;
import com.awinson.service.UserService;
import com.awinson.valid.ApiKeyValid;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        return "/user/setting";
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

        //TODO 获取BITVC资产信息

        model.addAttribute("map", map);
        return "/user/account";
    }

}
