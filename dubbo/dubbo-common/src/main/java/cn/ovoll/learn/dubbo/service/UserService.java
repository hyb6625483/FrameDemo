package cn.ovoll.learn.dubbo.service;

import cn.ovoll.learn.dubbo.entity.User;
import com.github.pagehelper.PageInfo;

public interface UserService {
    User findUser(Integer userId) throws Exception;

    PageInfo findUserList(Integer pageNo,Integer pageSize) throws Exception;
}
