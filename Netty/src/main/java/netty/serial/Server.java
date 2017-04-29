package netty.serial;

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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.util.GzipUtils;
import org.jboss.marshalling.Marshalling;

import java.io.File;
import java.io.FileOutputStream;

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
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32*1024)
                    .childOption(ChannelOption.SO_RCVBUF, 32*1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置Marshalling编解码器
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
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


    private static class ServerHandle extends SimpleChannelInboundHandler<RequestObject> {

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
        protected void channelRead0(ChannelHandlerContext ctx, RequestObject msg) throws Exception {
            System.out.println("收到消息：：："+msg);

            byte[] ungzip = GzipUtils.ungzip(msg.getAttachment());
            String path = System.getProperty("user.dir") + File.separatorChar + "Netty" +File.separatorChar +"file"
                    +File.separatorChar + "receive" +  File.separatorChar + msg.getName()+".jpg";

            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(ungzip);
            outputStream.close();

            ResponseObject response = new ResponseObject();
            response.setId("ID:"+msg.getId());
            response.setName("Name:"+msg.getName());
            response.setResponseMessage("ResponseMessage:"+msg.getRequestMessage());
            ctx.channel().writeAndFlush(response);
        }
    }
}
