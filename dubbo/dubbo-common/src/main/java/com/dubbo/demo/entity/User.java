package com.dubbo.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer userId;

    private String loginName;

    private String password;

    private Date createTime;
}