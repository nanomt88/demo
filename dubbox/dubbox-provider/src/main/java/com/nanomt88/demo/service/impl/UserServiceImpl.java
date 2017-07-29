package com.nanomt88.demo.service.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.nanomt88.demo.common.User;
import com.nanomt88.demo.service.UserService;
import org.springframework.stereotype.Service;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 *    模拟 REST风格接口
 *      对外发布的REST URL： http://localhost:8888/provider/user/testget
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-24 8:07
 **/
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = com.nanomt88.demo.service.UserService.class,
    protocol = {"dubbo","rest"}, retries = 0)
@Path(value = "/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class UserServiceImpl implements UserService {

    @GET
    @Path("/testget")
    public void testGet() {
        System.out.println("testGet invoke....");
    }

    @GET
    @Path("/getUser")
    public User getUser() {
        User u = new User(100L, "张三", 39L);
        return u;
    }

    @GET
    @Path("/get/{id : \\d+}")
    public User getUser(@PathParam(value = "id") Long id) {
        return new User(id, "张三", 39L);
    }

    @GET
    @Path("/get/{id : \\d+}/{name : [a-zA-Z][0-9]}")
    public User getUser(@PathParam(value = "id") Long id, @PathParam(value = "name") String name) {
        return new User(id, name, 100L);
    }

    @POST
    @Path("/testpost")
    public void testpost() {
        System.out.println("testPost invoke....");
    }

    @POST
    @Path("/postUser")
    public User postUser(User user) {
        System.out.println("入参：" + user);
        return new User(99L, "张三", 39L);
    }


    @POST
    @Path("/post/{id}")
    public User postUser(@PathParam(value = "id") String id) {
        return new User(Long.parseLong(id), "三三", 35L);
    }
}
