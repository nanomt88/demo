package com.nanomt88.demo.curator.samlpe;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-22 9:37
 **/
public class CuratorDemo {

    /** zookeeper服务器地址 */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /** 定义session失效时间 */
    public static final int SESSION_TIMEOUT = 30000;

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

        System.out.println(cf.getState());

        //新增
        //建立节点  指定节点类型（不加withMode默认为持久化类型）、路径、数据内容
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/super/c1","c1内容".getBytes());
        //删除节点
        cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");

        //读取、修改
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/super/c1","c1内容".getBytes());

        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/super/c2","c2内容".getBytes());

        //读取数据
        String data =  new String(cf.getData().forPath("/super/c1"));
        System.out.println("/super/c1 ===========> "+data);
        //修改节点
        cf.setData().forPath("/super/c2", "修改C2de内容".getBytes());
        System.out.println("/super/c2 ===========> "+ new String(cf.getData().forPath("/super/c2")));


        //绑定回调函数  : 在创建之后进行回调本地函数
        ExecutorService executorService = Executors.newCachedThreadPool();
        cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground((curatorFramework, curatorEvent) -> {
                    System.out.println("code: " + curatorEvent.getResultCode());
                    System.out.println("type: " + curatorEvent.getType());
                    System.out.println("线程为：" + Thread.currentThread().getName());
        }, executorService)
                .forPath("/super/c3", "我是C3 内容".getBytes());
        Thread.sleep(5000);

        //读取子节点 getChildren方法  和 判断节点是否存在 checkExits方法
        List<String> list = cf.getChildren().forPath("/super");
        for(String path : list){
            System.out.println("/super children ==============> " + path);
        }

        Stat stat = cf.checkExists().forPath("/super/c3");
        System.out.println("/super/c3  stat  ==================>" + stat);

        Thread.sleep(2000);

        //删除super 下面所有的节点
        cf.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");

        executorService.shutdown();
        //关闭连接
        cf.close();
    }
}
