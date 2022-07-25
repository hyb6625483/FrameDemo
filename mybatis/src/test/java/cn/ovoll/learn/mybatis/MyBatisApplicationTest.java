package cn.ovoll.learn.mybatis;

import cn.ovoll.learn.mybatis.entity.User;
import cn.ovoll.learn.mybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class MyBatisApplicationTest {

    @Resource
    private UserService userService;

    @Test
    void getUserList() {
        List<User> list = userService.getUserListByExample("zhangsan");
        for (User user : list) {
            System.out.println(user.getLoginName() + ": " + user.getUserId());
        }
    }

    @Test
    void getUserById() {
        User user = userService.getUserByExample(1);
        System.out.println(user.getLoginName() + ": " + user.getUserId());
    }
}
