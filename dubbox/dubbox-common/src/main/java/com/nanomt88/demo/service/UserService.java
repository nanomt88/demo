package com.nanomt88.demo.service;

import com.nanomt88.demo.common.User;


/**
 * 模拟 REST风格接口
 *      对外发布的REST URL： http://localhost:8888/provider/user/testget
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-24 7:31
 **/

public interface UserService{


    void testGet();


    User getUser();


    User getUser( Long id);


    User getUser(Long id, String name);


    void testpost();


    User postUser(User user);


    User postUser(String id);
}
