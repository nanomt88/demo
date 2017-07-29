package com.nanomt88.demo.curator.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Random;

/**
 *  Curator  Barrier 示例      DistributedBarrier :
 *      单向 barrier ，所有线程都等待一个信号量后，同时运行；
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-22 16:18
 **/
public class CuratorBarrier2 {

    /**
     * zookeeper服务器地址
     */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /**
     * 定义session失效时间
     */
    public static final int SESSION_TIMEOUT = 5000;

    /**
     * 屏障
     */
    static  DistributedBarrier barrier = null;

    public static void main(String[] args) throws Exception {

        //准备5个线程一起运行
        for (int i = 0; i < 5; i++) {

            new Thread(() -> {

                try {
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
                    System.out.println(Thread.currentThread().getName() + " - " + cf.getState());

                    barrier = new DistributedBarrier(cf, "/lock");

                    System.out.println(Thread.currentThread().getName() + " - 设置barrier，等待执行 !!!");

                    //此时插入屏障，等待外层发送信号之后开始执行
                    barrier.setBarrier();
                    barrier.waitOnBarrier();

                    System.out.println(Thread.currentThread().getName() + "---------开始执行程序----------");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, "t" + i).start();

        }

        Thread.sleep(1000*20);
        //释放信号量，让线程内等待的线程 一起执行
        barrier.removeBarrier();
    }
}
