package com.nanomt88.demo.zookeeper.cluster;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 客户端监控节点变化的事件
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-21 20:53
 **/
public class ZKWatcher implements Watcher{
    /**
     * zookeeper 实例
     */
    private ZooKeeper zk = null;
    /**
     * 父节点 path
     */
    static final String ZK_PARENT_PATH = "/super";

    /**
     * 信号量，用于阻塞主程序运行，等待连接zookeeper成功之后，发送成功信号后唤醒主程序继续运行
     */
    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    /**
     * 保存父节点下面的所有子节点信息
     */
    private List<String> childrenNodes = new ArrayList<>();

    /** zookeeper服务器地址 */
    public static final String CONNECTION_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";

    /** 定义session失效时间 */
    public static final int SESSION_TIMEOUT = 30000;

    public ZKWatcher() throws Exception {
        zk = new ZooKeeper(CONNECTION_ADDR, SESSION_TIMEOUT, this);
        System.out.println("开始连接Zookeeper服务器");
        connectedSemaphore.await();
    }

    @Override
    public void process(WatchedEvent event) {
        //连接状态
        Event.KeeperState state = event.getState();
        //事件类型
        Event.EventType eventType = event.getType();

        //受影响的path
        String path = event.getPath();
        System.out.println("受影响的path："+path);

        if(Event.KeeperState.SyncConnected == state){
            //成功连接上服务器
            if(Event.EventType.None == eventType){
                System.out.println("成功连接上ZK服务器");
                //释放连接成功信号量
                connectedSemaphore.countDown();

                try {
                    if (zk.exists(ZK_PARENT_PATH, false) == null) {
                        zk.create(ZK_PARENT_PATH, "this is root".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                CreateMode.PERSISTENT);
                    }

                    //获取子节点， 并且监控父节点变化
                    List<String> children = zk.getChildren(ZK_PARENT_PATH, true);
                    for(String child : children){
                        System.out.println("/super  has children : " + child);
                        //watch参数为true， 监控子节点变化
                        zk.exists(ZK_PARENT_PATH + "/" + child ,true);
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }else if(Event.EventType.NodeCreated == eventType){ //创建节点
                System.out.println("节点创建: "+path);
                try {
                    zk.exists(path, true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(Event.EventType.NodeDataChanged == eventType){ //更新节点
                System.out.println("节点数据更新: "+path);
                try {
                    zk.exists(path, true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(Event.EventType.NodeChildrenChanged == eventType){ //子节点变更
                System.out.println("子节点 变更："+path);

                try {
                    //获取子节点
                    List<String> children = zk.getChildren(path, true);
                    if(children.size() >= childrenNodes.size()){
                        children.removeAll(childrenNodes);
                        for (String child : children) {
                            System.out.println("/super  has children : " + child);
                            //watch参数为true， 监控子节点变化
                            zk.exists(path + "/" + child, true);
                            System.out.println("新增子节点： " + path + "/" + child);
                        }
                        childrenNodes.addAll(children);
                    }else{
                        childrenNodes = children;
                    }

                }catch (InterruptedException | KeeperException e) {
                    e.printStackTrace();
                }

            }else if(Event.EventType.NodeDeleted == eventType){ //节点被删除
                System.out.println("节点被删除：" + path);
                try {
                    zk.exists(path, true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else if (Event.KeeperState.Disconnected == state){
            System.out.println("与ZK服务器断开连接");
        }else if (Event.KeeperState.AuthFailed == state)
            System.out.println("权限检查失败");
        else if (Event.KeeperState.Expired == state)
            System.out.println("会话失效");
        else ;
        System.out.println(" ----------------------------------------- ");
    }
}
