package netty.pojo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by ZBOOK-17 on 2017/3/31.
 */
public class TimeServer {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new TimeServerHandle());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture channelFuture = bootstrap.bind(8080).sync();

            System.out.println("Server started ... ");

            channelFuture.channel().closeFuture().sync();
            System.out.println("Server stop ... ");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}
    class TimeServerHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("=============>client "+ctx.channel().remoteAddress()+" login in ...");
        ChannelFuture future = ctx.pipeline().writeAndFlush(new UnixTime());
        //添加关闭连接的监听
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read========>"+msg);
    }
}
