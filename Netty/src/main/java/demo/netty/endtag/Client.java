package demo.netty.endtag;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;

/**
 * Created by ZBOOK-17 on 2017/4/20.
 */
public class Client {
    private static final String END_TAG = "$_$";

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer(END_TAG.getBytes());
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,buf));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ClientHandle());
                        }
                    });



            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();

            future.channel().writeAndFlush(Unpooled.wrappedBuffer(("aaaa"+END_TAG+"cccc"+END_TAG).getBytes()));
            future.channel().writeAndFlush(Unpooled.wrappedBuffer(("bbbb"+END_TAG).getBytes()));

            //关闭端口和连接
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println(msg);
        }
    }
}
