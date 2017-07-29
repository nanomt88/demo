package demo.client;

import demo.netty.autoconnection.RequestObject;
import demo.netty.autoconnection.ResponseObject;
import demo.netty.util.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/7/28 下午3:03
 * @Description: //TODO
 */

public class BeijingPayClient {

    EventLoopGroup group = null;
    Bootstrap bootstrap = null;
    ChannelFuture future = null;

    private int port ;
    private String ip;
    public BeijingPayClient(String ip, int port){
        this.port = port;
        this.ip = ip;
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 32*1024)
                .option(ChannelOption.SO_RCVBUF, 32*1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                        pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                        pipeline.addLast(new BeijingPayClient.ClientHandle());
                    }
                });

    }

    /**
     * 连接远程服务器
     */
    public void connect(){
        try {
            future = bootstrap.connect(ip, port).sync();
            System.out.println("连接断开，重新连接到远程服务器："+ip+" : "+ port);
        } catch (InterruptedException e) {
            System.out.println("连接断开，重新连接到远程服务器："+ip+" : "+ port + " 异常");
            e.printStackTrace();
        }
    }

    public ChannelFuture getFutureChannel(){
        //判断，为空则自动重连，使用double check 的方法，防止并发连接
        if(future == null){
            synchronized (this) {
                if(future == null) {
                    //连接远程服务器
                    connect();
                }
            }
        }
        //如果连接不可用，也自动重连
        if(!future.channel().isActive()){
            synchronized (this) {
                if(!future.channel().isActive()) {
                    connect();
                }
            }
        }
        return future;
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<RequestMessage> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
            System.out.println("=============>"+msg);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);
            System.out.println("连接关闭。。。");
        }
    }


    public static void main(String[] args) {
        final BeijingPayClient client = new BeijingPayClient("127.0.0.1",8080);

        try {
            for (int i = 0; i < 5; i++) {
                RequestMessage req = new RequestMessage();
                req.setId("" + i);
                req.setName("pic" + i);
                req.setRequestMessage("我是client：" + i);

                client.getFutureChannel().channel().writeAndFlush(req);
                TimeUnit.SECONDS.sleep(3);
            }
            client.getFutureChannel().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
