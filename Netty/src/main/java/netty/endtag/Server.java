package netty.endtag;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by ZBOOK-17 on 2017/4/19.
 */
public class Server {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            //创建线程组

            //创建服务器辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32*1024)
                    .childOption(ChannelOption.SO_RCVBUF, 32*1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置分隔符
                            ByteBuf buf = Unpooled.copiedBuffer("$_$".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 , buf));
                            //设置字符串解码、编码器
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ServerHandle());
                        }
                    });
            //绑定端口
            ChannelFuture future = bootstrap.bind(8080).sync();

            //等待服务器监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //关闭线程组  释放资源
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }


    private static class ServerHandle extends SimpleChannelInboundHandler<String> {

        private static final String END_TAG = "$_$";

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client:"+ctx.channel().remoteAddress()+" active...");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            //关闭连接
            ctx.close();
        }

        /**
         * SimpleChannelInboundHandler 的channelRead0方法，会自动清空缓存
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("Client say:"+msg);
            String response = "服务器接受到消息：" + msg + END_TAG;
            //回写信息
            ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        }
    }
}
