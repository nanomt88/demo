package com.nanomt88.demo.zookeeper.sample;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *   zookeeper 原生jar包 demo
 *      注意： zk第一次连接成功之后，是没有返回事件类型的；这个时候表示连接成功。
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-21 7:06
 **/
public class ZookeeperDemo {

    /**
     * zookeeper 连接地址，中间使用 分号分隔
     */
    static final String CONNECT_ADDR =  "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";
    /**
     * session超时时间
     */
    static final int SESSION_TIMEOUT = 2000;    //ms
    /**
     * 信号量，用于阻塞主程序运行，等待连接zookeeper成功之后，发送成功信号后唤醒主程序继续运行
     */
    static final CountDownLatch semaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_TIMEOUT, event -> {
            //获取事件的状态
            KeeperState state = event.getState();
            //获取事件的类型
            EventType eventType = event.getType();
            if(KeeperState.SyncConnected == state) {

                //// PS : 此处注意： 第一次链接成功之后，是没有事件类型的： 表示链接成功

                if(EventType.None == eventType){
                    //如果建立连接成功，则发送信号量，让后续堵塞的程序继续运行
                    semaphore.countDown();
                    System.out.println("Zookeeper 建立连接成功");
                }
            }
        });

        //主程序堵塞，等待zk连接成功之后继续运行
        semaphore.await();

        //创建父节点
//        zk.create("/test", "this is my first test Root".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT);

//        zk.create("/test/children", "this is test root children".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT);

        //获取节点数据；   第二个参数 watch：为监控，除非需要监控 一般都为false
        byte[] data = zk.getData("/test", false, null);
        System.out.println("/test   :   " + new String(data));

        System.out.println("/test children   :   " + zk.getChildren("/test", false));

        //修改节点值
        zk.setData("/test", "modify test value".getBytes(), -1);
        byte[] data2 = zk.getData("/test", false, null);
        System.out.println("/test   :   " + new String(data2));

        //判断节点是否存在
        System.out.println("exists /test/children   :   " + zk.exists("/test/children", false));

        //删除节点
        zk.delete("/test/children", -1);
        System.out.println("exists /test/children   :   " + zk.exists("/test/children", false));

        zk.close();
    }
}
