package com.mybatis.demo.service.impl;

import com.mybatis.demo.entity.User;
import com.mybatis.demo.entity.UserExample;
import com.mybatis.demo.mapper.UserMapper;
import com.mybatis.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public List<User> getUserListByExample(String loginName) {
        try {
            UserExample userExample = new UserExample();
            UserExample.Criteria criteria = userExample.createCriteria();
            criteria.andLoginNameLike("%" + loginName + "%");
            return userMapper.selectByExample(userExample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByExample(Integer userId) {
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
