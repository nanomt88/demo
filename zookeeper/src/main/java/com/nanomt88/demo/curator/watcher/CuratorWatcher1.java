package com.nanomt88.demo.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 *    Curator watcher第一种监听方式：
 *     NodeCache ： 只能监听节点的 新增 和 修改事件； 不能监听到节点的删除事件
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-22 12:56
 **/
public class CuratorWatcher1 {

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
        System.out.println("zookeeper建立连接");
        //建立一个 cache缓存
        //第三个参数： 表示受否进行压缩；true表示进行压缩
        final NodeCache cache = new NodeCache(cf, "/super", false);

        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            /**
             *  触发事件为创建节点和更新节点，在删除节点的时候并不触发此操作
             * @throws Exception
             */
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("路径为：" + cache.getCurrentData().getPath());
                System.out.println("数据为：" + new String(cache.getCurrentData().getData()));
                System.out.println("状态为：" + cache.getCurrentData().getStat());
                System.out.println("---------------------------------------------");
            }
        });

        Thread.sleep(1000);
        cf.create().forPath("/super", "123".getBytes());

        Thread.sleep(1000);
        cf.setData().forPath("/super", "我的哥".getBytes());

        Thread.sleep(1000);
        cf.delete().forPath("/super");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
