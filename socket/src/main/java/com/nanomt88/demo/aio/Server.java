package com.nanomt88.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/15 下午9:37
 * @Description: //TODO
 */

public class Server {

    //线程池
    private ExecutorService executorService;
    //
    private AsynchronousChannelGroup threadGroup;
    //服务器通道
    public AsynchronousServerSocketChannel serverSocketChannel;

    public Server(int port){
        try {
            //创建一个缓存池
            executorService = Executors.newCachedThreadPool();
            //创建线程组
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            //创建服务器通道
            serverSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);
            //进行绑定
            serverSocketChannel.bind(new InetSocketAddress(port));

            System.out.println("server started ... ");
            //进行堵塞
            serverSocketChannel.accept(this,new ServerCompletionHandler());
            //一直堵塞，不让服务器停止
            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server s = new Server(8888);
    }
}
