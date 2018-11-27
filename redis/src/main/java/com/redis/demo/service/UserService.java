package com.redis.demo.service;

import com.redis.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUserListByExample(String loginName);

    User getUserByExample(Integer userId);
}
