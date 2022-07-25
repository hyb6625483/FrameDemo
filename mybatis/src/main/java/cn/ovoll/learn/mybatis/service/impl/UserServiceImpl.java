package cn.ovoll.learn.mybatis.service.impl;

import cn.ovoll.learn.mybatis.service.UserService;
import cn.ovoll.learn.mybatis.entity.User;
import cn.ovoll.learn.mybatis.entity.UserExample;
import cn.ovoll.learn.mybatis.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
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

    @Override
    public User getUserByExample(Integer userId) {
        try {
            return userMapper.selectByPrimaryKey(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
