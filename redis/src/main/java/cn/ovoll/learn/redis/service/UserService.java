package cn.ovoll.learn.redis.service;

import cn.ovoll.learn.redis.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUserListByExample(String loginName);

    User getUserByExample(Integer userId);
}
