package com.dubbo.demo.service;

import com.dubbo.demo.entity.User;
import com.github.pagehelper.PageInfo;

public interface UserService {
    User findUser(Integer userId) throws Exception;

    PageInfo findUserList(Integer pageNo,Integer pageSize) throws Exception;
}
