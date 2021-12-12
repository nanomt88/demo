package demo.work.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Timer;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/7/28 下午3:03
 * @Description: //TODO
 */

public class BeijingPayClient {


    /**
     * 服务器辅助类
     */
    Bootstrap bootstrap = null;
    /**
     * netty 线程组
     */
    EventLoopGroup group = null;
    /**
     * channel
     */
    ChannelFuture future = null;

    /**
     * 心跳间隔时间
     */
    private static long HEARTBEAT_TIME = 10 ;

    private String ip;

    private int port;

    public BeijingPayClient(String ip, int port) {
        this.port = port;
        this.ip = ip;
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送缓冲区的大小
                .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受缓冲区的大小
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new XmlEncode(), new BeijingPayClient.ClientHandle());
                    }
                });
        final Timer timer = new Timer("HeartbeatTimer", true);
        timer.schedule(new HeartbeatTimer(this), HEARTBEAT_TIME, HEARTBEAT_TIME * 1000L); //10秒 执行一次
    }

    /**
     * 连接远程服务器
     */
    public void connect() throws Exception {
        try {
            future = bootstrap.connect(ip, port).sync();
            System.out.println("连接断开，重新连接到远程服务器：" + ip + " : " + port);
        } catch (InterruptedException e) {
            System.out.println("连接断开，重新连接到远程服务器：" + ip + " : " + port + " 异常");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取 channel
     * @return
     * @throws Exception
     */
    private ChannelFuture getFutureChannel() throws Exception {
        //判断，为空则自动重连，使用double check 的方法，防止并发连接
        if (future == null) {
            synchronized (this) {
                if (future == null) {
                    //连接远程服务器
                    connect();
                }
            }
        }
        //如果连接不可用，也自动重连
        if (!future.channel().isActive()) {
            synchronized (this) {
                if (!future.channel().isActive()) {
                    connect();
                }
            }
        }
        return future;
    }

    /**
     * 发送信息
     * @param request
     * @param <T>
     * @throws Exception
     */
    public <T> void sendMessage(T request) throws Exception {
        System.out.println("send request to beijing pay : "  + request);
        getFutureChannel().channel().writeAndFlush(request);
    }

    private static class ClientHandle extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("=============>" + msg);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);
            System.out.println("连接关闭: " + ctx.channel().remoteAddress());
        }
    }

}
