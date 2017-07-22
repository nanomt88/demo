package com.nanomt88.demo.curator.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 分布式锁实例
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-22 15:03
 **/
public class ZKLock {

    /** zookeeper服务器地址 */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /** 定义session失效时间 */
    public static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws InterruptedException {

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

        System.out.println(cf.getState());

        //4  分布式锁
        final InterProcessMutex multiLock = new InterProcessMutex(cf, "/lock");
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        for(int i=0; i< 10; i++){
            new Thread(() -> {
                try {

                    countDownLatch.await();
                    //加分布式锁
                    multiLock.acquire();
                    // --------业务处理开始----------
                    //模拟业务生成流水号
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    System.out.println(Thread.currentThread().getName() + " - " + sdf.format(new Date()));
                    // --------业务处理结束----------

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //释放分布式锁
                        multiLock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }, "t"+i).start();
        }

        Thread.sleep(1000);
        countDownLatch.countDown();

        Thread.sleep(10000);
    }
}
