package com.redis.demo.controller;

import com.redis.demo.entity.User;
import com.redis.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class RedisController {

    @Resource
    private UserService userService;

    @RequestMapping("/hello")
    public String sayHi() {
        return "hello";
    }

    @RequestMapping("/findUserList")
    public List<User> findUserList(String login) {
        return userService.getUserListByExample(login);
    }

    @RequestMapping("/findUser")
    public User findUser(Integer userId){
        return userService.getUserByExample(userId);
    }
}
