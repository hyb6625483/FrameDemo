package cn.ovoll.learn.mybatis.service;

import cn.ovoll.learn.mybatis.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUserListByExample(String loginName);

    User getUserByExample(Integer userId);
}
