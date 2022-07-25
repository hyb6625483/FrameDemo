package cn.ovoll.learn.dubbo.service.impl;

import cn.ovoll.learn.dubbo.entity.User;
import com.alibaba.dubbo.config.annotation.Service;
import cn.ovoll.learn.dubbo.example.UserExample;
import cn.ovoll.learn.dubbo.mapper.UserMapper;
import cn.ovoll.learn.dubbo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;

@Service(version = "1.0")
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
