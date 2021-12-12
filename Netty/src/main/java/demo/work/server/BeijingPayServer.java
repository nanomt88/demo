package demo.work.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/7/28 下午3:04
 * @Description: //TODO
 */

public class BeijingPayServer {

    public BeijingPayServer(int port) {

        //创建线程组
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            //创建服务器辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，
                    // 服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32*1024)   //设置发送缓冲区的大小
                    .childOption(ChannelOption.SO_RCVBUF, 32*1024)   //设置接受缓冲区的大小
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ReadTimeoutHandler(30));   //30秒内无数据，自动断开连接
                            //设置自定义XML编解码器 和 处理数据的handler
                            ch.pipeline().addLast(new XmlDecoder(), new BeijingPayServerHandler());
                        }
                    });
            //绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();

            System.out.println("server started...");

            //等待服务器监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println("server close in exception");
            e.printStackTrace();
        } finally {
            //关闭线程组  释放资源

            //注意 ： 先关闭boss 再关闭work
            //TODO  关闭方法是不是放在这 暂时还不确定，需要优雅关闭 应该不是写在这
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
