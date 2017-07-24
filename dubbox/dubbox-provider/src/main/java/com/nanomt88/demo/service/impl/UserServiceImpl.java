package com.nanomt88.demo.service.impl;

import com.nanomt88.demo.common.User;
import com.nanomt88.demo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-24 8:07
 **/
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = com.nanomt88.demo.service.UserService.class,
    protocol = {"dubbo","rest"}, retries = 0)
public class UserServiceImpl implements UserService {

    @Override
    public void testGet() {
        System.out.println("testGet invoke....");
    }

    @Override
    public User getUser() {
        User u = new User(100L, "张三", 39L);
        return u;
    }

    @Override
    public User getUser(Long id) {
        return new User(id, "张三", 39L);
    }

    @Override
    public User getUser(Long id, String name) {
        return new User(id, name, 100L);
    }

    @Override
    public void testpost() {
        System.out.println("testPost invoke....");
    }

    @Override
    public User postUser(User user) {
        System.out.println("入参：" + user);
        return new User(99L, "张三", 39L);
    }

    @Override
    public User postUser(String id) {
        return new User(Long.parseLong(id), "三三", 35L);
    }
}
