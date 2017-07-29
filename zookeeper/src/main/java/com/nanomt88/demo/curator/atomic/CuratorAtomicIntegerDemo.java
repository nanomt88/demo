package com.nanomt88.demo.curator.atomic;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 *   Zookeeper 分布式原子递增计数器 demo
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-22 15:30
 **/
public class CuratorAtomicIntegerDemo {

    /** zookeeper服务器地址 */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /** 定义session失效时间 */
    public static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {

        //1. 重试策略： 初试时间为1秒，重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2  通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(CONNECTION_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .build();

        //3  开启连接
        cf.start();
        System.out.println("==============> "+cf.getState());

//        if(cf.checkExists().forPath("/lock") != null) {
//            System.out.println("删除节点 ： /lock");
//            cf.delete().forPath("/lock");
//        }

        //4. 使用分布式原子递增计数器
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(cf, "/lock", retryPolicy);

        AtomicValue<Integer> value = atomicInteger.add(1);
        System.out.println(value.succeeded());
        System.out.println(value.postValue());	//最新值 : 也就是自增之后的值
        System.out.println(value.preValue());	//原始值 ： 自增之前的值
    }

}
