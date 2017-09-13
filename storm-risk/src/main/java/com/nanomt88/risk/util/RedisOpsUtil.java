package com.nanomt88.risk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * 操作redis的工具类
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-05 22:36
 **/
public class RedisOpsUtil {

    private Jedis jedis;

    public RedisOpsUtil(String ip, int port){
        jedis = new Jedis(ip, port);
    }

    public void set(String id, Map<String, Object> map){
        jedis.set(id, JSONObject.toJSONString(map));
    }

    public <T> T get(String key, Class<T> clazz){
        if(clazz == String.class)
            return (T) jedis.get(key);
        else{
            return JSON.parseObject(jedis.get(key), clazz);
        }
    }
}
