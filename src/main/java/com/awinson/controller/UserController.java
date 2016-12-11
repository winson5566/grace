package com.awinson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by winson on 2016/12/8.
 */
@Controller
@RequestMapping("u")
public class UserController {

    /**
     * 用户的主页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("")
    public String index(HttpServletRequest request, Model model) {
        return "/user/index";
    }

    /**
     * 设置个人参数
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("setting")
    public String setting(HttpServletRequest request, Model model) {
        return "/user/setting";
    }
}
