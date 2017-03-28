package org.tcdd.netty.hellowold;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by ZBOOK-17 on 2017/3/28.
 */
public class DiscardServer {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,work);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(new DiscardServerHandle());
            }
        });
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            //绑定端口，并开始接受进来的线程
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();

            //等待服务 socket关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }


    }

    private static class DiscardServerHandle extends ChannelInboundHandlerAdapter  {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        }
    }
}
