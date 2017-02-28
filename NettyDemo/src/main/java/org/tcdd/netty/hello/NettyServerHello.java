package org.tcdd.netty.hello;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZBOOK-17 on 2017/2/27.
 */


public class NettyServerHello {

    public static void main(String[] args) {
        //创建服务器的启动器
        ServerBootstrap bootstrap = new ServerBootstrap();
        //创建线程池，链接管理的线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        //IO传输的连接池
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                //设置解码
                pipeline.addLast("decoder", new StringDecoder());
                //设置编码
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handle", new ServerHandle());
                return pipeline;
            }
        });
        //绑定端口
        bootstrap.bind(new InetSocketAddress(8888));
        System.out.println("server started...");
    }

}
