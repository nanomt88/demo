package com.nanomt88.demo.zookeeper.cluster;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.concurrent.CountDownLatch;

/**
 *  操作zookeeper
 * @author nanomt88@gmail.com
 * @create 2017-07-21 22:30
 **/
public class Test {

    /** zookeeper服务器地址 */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /** 定义session失效时间 */
    public static final int SESSION_TIMEOUT = 30000;
    /**
     * 信号量，用于阻塞主程序运行，等待连接zookeeper成功之后，发送成功信号后唤醒主程序继续运行
     */
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = new ZooKeeper(CONNECTION_ADDR, SESSION_TIMEOUT, event -> {
            //获取事件的状态
            KeeperState state = event.getState();
            //获取事件的类型
            EventType eventType = event.getType();
            if(KeeperState.SyncConnected == state) {

                //// PS : 此处注意： 第一次链接成功之后，是没有事件类型的： 表示链接成功

                if(EventType.None == eventType){
                    //如果建立连接成功，则发送信号量，让后续堵塞的程序继续运行
                    connectedSemaphore.countDown();
                    System.out.println("Zookeeper 建立连接成功");
                }
            }
        });

        //主程序堵塞，等待zk连接成功之后继续运行
        connectedSemaphore.await();

        //		//创建子节点
		zk.create("/super/c1", "c1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //创建子节点
		zk.create("/super/c2", "c2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //创建子节点
        zk.create("/super/c3", "c3".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //创建子节点
		zk.create("/super/c4", "c4".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		zk.create("/super/c4/c44", "c44".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

//        		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));
//		System.out.println(zk.getChildren("/testRoot", false));

        //修改节点的值
		zk.setData("/super/c1", "modify c1".getBytes(), -1);
		zk.setData("/super/c2", "modify c2".getBytes(), -1);
		byte[] data = zk.getData("/super/c2", false, null);
		System.out.println(new String(data));

//		//判断节点是否存在
		System.out.println(zk.exists("/super/c3", false));
//		//删除节点
        zk.delete("/super/c3", -1);

        zk.close();

    }
}
