package com.nanomt88.demo.zookeeper.cluster;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-21 22:27
 **/
public class Client2 {

    public static void main(String[] args) throws Exception {
        ZKWatcher watcher = new ZKWatcher();
        System.out.println("client2 started ....");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
