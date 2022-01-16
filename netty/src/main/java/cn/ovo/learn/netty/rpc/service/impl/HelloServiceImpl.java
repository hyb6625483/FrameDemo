package cn.ovo.learn.netty.rpc.service.impl;

import cn.ovo.learn.netty.rpc.service.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello, " + name;
    }
}
