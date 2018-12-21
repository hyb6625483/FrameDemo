package com.dubbo.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Reference(version = "1.0",timeout = 5000)
    private UserService userService;

    @RequestMapping("/findUser")
    public String findUser(Integer userId, Model model) {
        try {
            /*User user = userService.findUser(userId);
            modelMap.addAttribute("name",user.getLoginName());
            modelMap.addAttribute("createTime",user.getLoginName());*/
            model.addAttribute("user", userService.findUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @RequestMapping("/findUserList")
    public String findUserList(ModelMap modelMap) {
        try {
            modelMap.addAttribute("userList", userService.findUserList(1, 10).getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
}
