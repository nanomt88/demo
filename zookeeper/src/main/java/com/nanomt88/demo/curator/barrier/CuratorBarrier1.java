package com.nanomt88.demo.curator.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Random;

/**
 *  Curator  Barrier 示例      DistributedDoubleBarrier :
 *      双向 barrier ，即在进入前插入屏障，然后所有线程都准备好之后一起运行；
 *      执行完成之后也会插入屏障，等所有线程都执行完成之后一起释放
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-22 16:18
 **/
public class CuratorBarrier1 {

    /**
     * zookeeper服务器地址
     */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /**
     * 定义session失效时间
     */
    public static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws InterruptedException {

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

                    DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(cf, "/lock", 5);

                    //让线程随机睡眠一段时间
                    int time = new Random().nextInt(5) * 1000;
                    System.out.println(Thread.currentThread().getName() + " - 休眠：" + time);
                    Thread.sleep(time);

                    System.out.println(Thread.currentThread().getName() + " - 准备好了");
                    //此时插入屏障，等待所有线程都准备好了之后一起执行
                    barrier.enter();
                    System.out.println(Thread.currentThread().getName() + " - 开始运行……");
                    Thread.sleep(new Random().nextInt(5) * 1000);

                    //此处插入屏障，等所有线程都执行完毕之后一起释放
                    barrier.leave();
                    System.out.println(Thread.currentThread().getName() + " - 退出……");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, "t" + i).start();

        }
    }
}
