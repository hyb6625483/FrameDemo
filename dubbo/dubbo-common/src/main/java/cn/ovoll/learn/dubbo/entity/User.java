package cn.ovoll.learn.dubbo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private Integer userId;

    private String loginName;

    private String password;

    private Date createTime;
}