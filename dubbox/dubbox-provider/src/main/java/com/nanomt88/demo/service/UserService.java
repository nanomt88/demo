package com.nanomt88.demo.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.nanomt88.demo.common.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 模拟 REST风格接口
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-24 7:31
 **/
@Path(value = "/user")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface UserService{

    @GET
    @Path("/testget")
    void testGet();

    @GET
    @Path("/getUser")
    User getUser();

    @GET
    @Path("/get/{id : \\d+}")
    User getUser(@PathParam(value = "id") Long id);

    @GET
    @Path("/get/{id : \\d+}/{name : [a-zA-Z][0-9]}")
    User getUser(@PathParam(value = "id") Long id, @PathParam(value = "name") String name);

    @POST
    @Path("/testpost")
    void testpost();

    @POST
    @Path("/postUser")
    User postUser(User user);

    @POST
    @Path("/post/{id}")
    User postUser(@PathParam(value = "id") String id);
}
