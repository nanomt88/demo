package netty.timeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;

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
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new TimeServerHandle());
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

            ByteBuf buffer = ctx.alloc().buffer(4);
            //这里要加2208988800，是因为获得到的时间是格林尼治时间，所以要变成东八区的时间，否则会与与北京时间有8小时的时差
            buffer.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

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
            System.out.println("error-----------------");
            cause.printStackTrace();
            ctx.close();
        }
    }
}
