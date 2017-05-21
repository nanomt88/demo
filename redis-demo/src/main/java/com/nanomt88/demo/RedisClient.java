package com.nanomt88.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ZBOOK-17 on 2017/5/21.
 */
public class RedisClient {

    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private static final String KEYS_STRING = "STRING";
    private static final String KEYS_SET = "SET";
    private static final String KEYS_LIST = "LIST";
    private static final String KEYS_HASH = "HASH";
    private static final String KEYS_ZSET = "ZSET";

    private static JedisCluster jedis = null;

    private static void addKey(final String conainter, final String key) {
        if (!jedis.exists(conainter)) {
            jedis.sadd(conainter, key);
        } else {
            if (!jedis.smembers(conainter).contains(key)) {
                jedis.sadd(conainter, key);
            }
        }
    }

    /**
     * 写入字符串缓存
     *
     * @param key
     * @param value
     * @return
     */
    private static String set(final String key, final String value) {
        String result = jedis.set(key, value);
        addKey(KEYS_STRING, key);
        return result;
    }


    /**
     * 写入Set缓存
     *
     * @param key
     * @param member
     * @return
     */
    private static Long sadd(final String key, final String... member) {
        Long result = jedis.sadd(key, member);
        addKey(KEYS_SET, key);
        return result;
    }

    /**
     * 从左侧写入List
     *
     * @param key
     * @param string
     * @return
     */
    private static Long lpush(final String key, final String... string) {
        Long result = jedis.lpush(key, string);
        addKey(KEYS_LIST, key);
        return result;
    }

    /**
     * 写入HashMap缓存
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    private static Long hset(final String key, final String field, final String value) {
        Long result = jedis.hset(key, field, value);
        addKey(KEYS_HASH, key);
        return result;
    }

    /**
     * 写入ZSet缓存
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    private static Long zadd(final String key, final double score, final String member) {
        Long result = jedis.zadd(key, score, member);
        addKey(KEYS_ZSET, key);
        return result;
    }

    private static Long zadd(final String key, final String member) {
        Long result = jedis.zadd(key, 0d, member);
        addKey(KEYS_ZSET, key);
        return result;
    }

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-redis.xml");

        jedis = ctx.getBean(JedisCluster.class);

        Map<String, JedisPool> nodes = jedis.getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : nodes.entrySet()) {
            logger.info(entry.getKey() + " => " + entry.getValue().toString());
            //清空所有数据
            try {
                entry.getValue().getResource().flushDB();
            } catch (Exception e) {
                logger.info(e.getLocalizedMessage());//slave节点上执行flushDB会报错
            }
            entry.getValue().getResource().keys("*");//慎用,缓存数量较大时,会引起性能问题.
        }

        //检测key是否存在
        logger.info(jedis.exists("a").toString());

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            jedis.set("NAME"+i, "value:"+i);
        }
        logger.info("============>set name 10000 takes : {} ms", (System.currentTimeMillis() - startTime));
        //字符串写入测试
        logger.info(set("a", "hello world!"));
        logger.info(set("b", "hello redis!"));

        //字符串读取测试
        logger.info(jedis.get("a"));

        //set写入操作
        logger.info("set写入测试 ==>");
        logger.info(sadd("set1", "a", "b", "c") + "");

        //缓存类型测试
        logger.info(jedis.type("set1"));

        //set读取测试
        logger.info("set读取测试 ==>");
        Set<String> set1 = jedis.smembers("set1");
        for (String s : set1) {
            logger.info(s);
        }

        //list写入测试
        logger.info("list写入测试 ==>");
        logger.info(lpush("list1", "1", "2", "3") + "");


        //list读取测试
        logger.info("list读取测试 ==>");
        List<String> list1 = jedis.lrange("list1", 0, 999);
        for (String s : list1) {
            logger.info(s);
        }

        //hash写入测试
        logger.info("hash写入测试 ==>");
        logger.info(hset("hash1", "jimmy", "杨俊明") + "");
        logger.info(hset("hash1", "CN", "中国") + "");
        logger.info(hset("hash1", "US", "美国") + "");

        //hash读取测试
        logger.info("hash读取测试 ==>");
        Map<String, String> hash1 = jedis.hgetAll("hash1");
        for (Map.Entry<String, String> entry : hash1.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }

        //zset写入测试
        logger.info("zset写入测试 ==>");
        logger.info(zadd("zset1", "3") + "");
        logger.info(zadd("zset1", "2") + "");
        logger.info(zadd("zset1", "1") + "");
        logger.info(zadd("zset1", "4") + "");
        logger.info(zadd("zset1", "5") + "");
        logger.info(zadd("zset1", "6") + "");

        //zset读取测试
        logger.info("zset读取测试 ==>");
        Set<String> zset1 = jedis.zrange("zset1", 0, 999);
        for (String s : zset1) {
            logger.info(s);
        }

        //遍历所有缓存项的key
        logger.info("遍历cluster中的所有key ==>");
        logger.info(KEYS_STRING+":{}", jedis.smembers(KEYS_STRING));
        logger.info(KEYS_HASH+":{}",jedis.smembers(KEYS_HASH));
        logger.info(KEYS_SET+":{}",jedis.smembers(KEYS_SET));
        logger.info(KEYS_LIST+":{}",jedis.smembers(KEYS_LIST));
        logger.info(KEYS_ZSET+":{}",jedis.smembers(KEYS_ZSET));

    }
}
