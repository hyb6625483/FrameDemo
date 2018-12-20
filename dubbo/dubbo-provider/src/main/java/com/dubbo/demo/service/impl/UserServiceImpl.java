package com.dubbo.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dubbo.demo.entity.User;
import com.dubbo.demo.entityExample.UserExample;
import com.dubbo.demo.mapper.UserMapper;
import com.dubbo.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;

@Service(version = "1.0",timeout = 5000)
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User findUser(Integer userId) throws Exception {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public PageInfo findUserList(Integer pageNo, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo(userMapper.selectByExample(new UserExample()));
    }
}
