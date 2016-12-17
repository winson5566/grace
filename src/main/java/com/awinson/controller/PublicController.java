package com.awinson.controller;

import com.awinson.Entity.User;
import com.awinson.repository.UserRepository;
import com.awinson.service.UserService;
import com.awinson.valid.RegisterValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by winson on 2016/12/8.
 */
@Controller
public class PublicController {


    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 查看公共模板common
     *
     * @return
     */
    @RequestMapping("/common")
    public String common() {
        return "common";
    }

    /**
     * 登陆页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping("/register")
    public String register() {

        return "register";
    }

    /**
     * 执行注册
     */
    @RequestMapping("/doRegister")
    public String doRegister(Model model, @Validated RegisterValid registerValid, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allErrors", bindingResult.getAllErrors());
            logger.info("格式错误，注册失败!");
            return ("register");
        }
        Map<String,Object> map = userService.register(registerValid);
        if ("0".equals(map.get("code"))){
            model.addAttribute("error", map.get("msg"));
            logger.info(map.get("msg").toString());
            return ("register");
        }
        logger.info("注册成功!");
        return ("redirect:/u");
    }

    /**
     * 403页面
     *
     * @return
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {
        ModelAndView model = new ModelAndView();
        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject("username", userDetail.getUsername());
        }
        model.setViewName("403");
        return model;
    }

    @RequestMapping(value="ws")
    public String wsTest(){
        return "ws";
    }


}
