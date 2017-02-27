package org.tcdd.netty.hello;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZBOOK-17 on 2017/2/27.
 */


public class NettyHello {

    public static void main(String[] args) {

        ServerBootstrap bootstrap = new ServerBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
    }

}
