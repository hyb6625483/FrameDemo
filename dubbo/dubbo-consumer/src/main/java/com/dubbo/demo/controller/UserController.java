package com.dubbo.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Reference(version = "1.0")
    private UserService userService;

    @RequestMapping("/user")
    public String findUser(Integer userId, ModelMap modelMap){
        try {
            modelMap.addAttribute("user",userService.findUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
}
