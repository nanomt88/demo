package netty.string;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ZBOOK-17 on 2017/2/28.
 */
public class  ClientDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        //实例化线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //实例化启动器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                //设置String字符串编码
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                //设置自定义处理类
                pipeline.addLast(new ClientHandleDemo());
            }
        });
        //连接服务端
        ChannelFuture connect = bootstrap.connect("127.0.0.1", 8080);
        //获取channel
        Channel channel = connect.sync().channel();
        BufferedReader in = new BufferedReader(new InputStreamReader( System.in ));
        while (true){
            String line = in.readLine();
            if(line == null)
                 break;
            if(line.contains("exit"))
                break;
            //往服务端写数据
            channel.writeAndFlush(line);
        }
        group.shutdownGracefully();
    }

    private static class ClientHandleDemo extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
            System.out.println("Server say : "+ o);
        }

        /**
         * 建立连接之后
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client connected ...");
            super.channelActive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("Client exceptionCaught");
            super.exceptionCaught(ctx, cause);
        }

        /**
         * 连接关闭之前
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("Client closed...");
            super.channelInactive(ctx);
        }
    }
}
