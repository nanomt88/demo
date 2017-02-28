package org.tcdd.netty.hello;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZBOOK-17 on 2017/2/27.
 */


public class NettyServerHello {

    public static void main(String[] args) {

        ServerBootstrap bootstrap = new ServerBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                //pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handle", new ServerHandle());
                return pipeline;
            }
        });

        bootstrap.bind(new InetSocketAddress(8888));
        System.out.println("server started...");
    }

}
