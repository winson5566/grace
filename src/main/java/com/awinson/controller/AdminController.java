package com.awinson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by winson on 2016/12/8.
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    /**
     * 管理员的主页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("")
    public String hellAdmin(HttpServletRequest request, Model model) {
        model.addAttribute("user",request.getRemoteUser());
        return "/admin/index";
    }
}
