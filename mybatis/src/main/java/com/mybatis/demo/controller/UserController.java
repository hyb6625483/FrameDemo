package com.mybatis.demo.controller;

import com.mybatis.demo.entity.User;
import com.mybatis.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/hello")
    public String sayHi() {
        return "welcome";
    }

    @RequestMapping("/findUserList")
    @ResponseBody
    public List<User> findUserList(String login) {
        return userService.getUserListByExample(login);
    }

    @RequestMapping("/findUser")
    @ResponseBody
    public User findUser(Integer userId){
        return userService.getUserByExample(userId);
    }
}
