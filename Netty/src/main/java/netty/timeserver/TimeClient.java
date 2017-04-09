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
                        .addLast(new TimeClientHandle2());
            }
        });

        Channel channel = bootstrap.connect("127.0.0.1", 8080).channel();

        //group.shutdownGracefully();
    }

    /**
     *  继承 ChannelInboundHandlerAdapter 类，需要自己手工释放 ByteBuf
     */
    private static class TimeClientHandle extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
            //System.out.println("server send msg:"+ s);
            ByteBuf buf = (ByteBuf) s;
            try {
                long time = buf.readUnsignedInt();
                System.out.println(time);
                //计算出当前时间的毫秒数
                long currentTimeMillis = (time - 2208988800L) * 1000L;
                System.out.println("==========>" + new Date(currentTimeMillis));
                ctx.close();
            }finally {
                buf.release();
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.out.println("error.....");
            cause.printStackTrace();
            ctx.close();
        }
    }

    /**
     * 继承 SimpleChannelInboundHandler 类，会自动释放bytebuf中的信息
     */
    private static class TimeClientHandle2 extends SimpleChannelInboundHandler {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception {
            //System.out.println("server send msg:"+ s);
            ByteBuf buf = (ByteBuf) s;
            try {
                long time = buf.readUnsignedInt();
                System.out.println(time);
                long currentTimeMillis = (time - 2208988800L) * 1000L;
                System.out.println("==========>" + new Date(currentTimeMillis));
                ctx.close();
            }finally {
                //buf.release();
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.out.println("error.....");
            cause.printStackTrace();
            ctx.close();
        }
    }
}
