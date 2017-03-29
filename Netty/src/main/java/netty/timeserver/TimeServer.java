package netty.timeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by ZBOOK-17 on 2017/3/29.
 */
public class TimeServer {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new TimeServerHandle());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128 )
                .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture channelFuture = bootstrap.bind(8080).sync();

            System.out.println("Server started ... ");

            channelFuture.channel().closeFuture().sync();
            System.out.println("Server stop ... ");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    private static class TimeServerHandle extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {

            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeLong(System.currentTimeMillis());

            final ChannelFuture future = ctx.writeAndFlush(buffer);
            future.addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    assert future == channelFuture ;
                    ctx.close();
                }
            });
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
