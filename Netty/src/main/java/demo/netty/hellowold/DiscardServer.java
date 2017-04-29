package demo.netty.hellowold;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by ZBOOK-17 on 2017/3/28.
 */
public class DiscardServer {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,work);

            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            //.addLast(new StringEncoder())
                            //.addLast(new StringDecoder())
                            .addLast(new DiscardServerHandle());
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口，并开始接受进来的线程
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();

            System.out.println("server started...");
            //等待服务 socket关闭
            channelFuture.channel().closeFuture().sync();
            System.out.println("server stop...");
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
            //((ByteBuf)msg).release();
            //或者
            try {
                ByteBuf in =((ByteBuf)msg);

                while (in.isReadable()){
                    System.out.print((char) in.readByte());
                    System.out.flush();
                }

                //System.out.println("Release message:"+ msg);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
    }
}
