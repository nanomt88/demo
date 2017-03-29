package netty.timeserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Created by ZBOOK-17 on 2017/3/29.
 */
public class TimeClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(new TimeClientHandle());
            }
        });

        Channel channel = bootstrap.connect("127.0.0.1", 8080).channel();

        //group.shutdownGracefully();
    }

    private static class TimeClientHandle extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
            //System.out.println("server send msg:"+ s);
            ByteBuf buf = (ByteBuf)s;
            long time = buf.readUnsignedInt();
            System.out.println(time);
            long currentTimeMillis = (time - 2208988800L) * 1000L;
            System.out.println("==========>"+new Date(currentTimeMillis));
            ctx.close();
            buf.release();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
