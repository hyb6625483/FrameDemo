package com.dubbo.demo.mapper;

import com.dubbo.demo.entity.User;
import com.dubbo.demo.entityExample.UserExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countByExample(UserExample example) throws Exception;

    int deleteByExample(UserExample example) throws Exception;

    int deleteByPrimaryKey(Integer userId) throws Exception;

    int insert(User record) throws Exception;

    int insertSelective(User record) throws Exception;

    List<User> selectByExample(UserExample example) throws Exception;

    User selectByPrimaryKey(Integer userId) throws Exception;

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example) throws Exception;

    int updateByExample(@Param("record") User record, @Param("example") UserExample example) throws Exception;

    int updateByPrimaryKeySelective(User record) throws Exception;

    int updateByPrimaryKey(User record) throws Exception;
}