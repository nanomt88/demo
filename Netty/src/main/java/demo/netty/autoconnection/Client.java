package demo.netty.autoconnection;

import demo.netty.util.GzipUtils;
import demo.netty.util.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZBOOK-17 on 2017/4/20.
 */
public class Client {

    EventLoopGroup group = null;
    Bootstrap bootstrap = null;
    ChannelFuture future = null;

    private int port ;
    private String ip;
    public Client(String ip, int port){
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
                        pipeline.addLast(new ClientHandle());
                    }
                });

    }

    /**
     * 连接远程服务器
     */
    public void connect(){
        try {
            future = bootstrap.connect(ip, port).sync();
            System.out.println("连接到远程服务器："+ip+" : "+ port);
        } catch (InterruptedException e) {
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

    public static void main(String[] args) {
        final Client client = new Client("127.0.0.1",8080);

        try {
            for(int i=0 ; i<5; i++){
                RequestObject req = new RequestObject();
                req.setId(""+i);
                req.setName("pic"+i);
                req.setRequestMessage("我是client："+i);

                client.getFutureChannel().channel().writeAndFlush(req);
                TimeUnit.SECONDS.sleep(3);
            }
            client.getFutureChannel().channel().closeFuture().sync();

            //主线程执行完成之后，关闭连接，子线程可以重连 然后进行数据读写
            new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println(Thread.currentThread().getName()+"   进入子线程....");
                    ChannelFuture f = client.getFutureChannel();
                    System.out.println(Thread.currentThread().getName()+ "  isActive:"+ f.channel().isActive());
                    System.out.println(Thread.currentThread().getName()+ "  isOpen:"+ f.channel().isOpen());

                    RequestObject req = new RequestObject();
                    req.setId("sub99");
                    req.setName("pic99");
                    req.setRequestMessage("我是client：99");

                    try {

                        f.channel().writeAndFlush(req);
                        f.channel().closeFuture().sync();
                        System.out.println(Thread.currentThread().getName()+"   关闭子线程");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }).start();

            System.out.println("断开连接,主线程结束..");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<ResponseObject> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ResponseObject msg) throws Exception {
            System.out.println(msg);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            super.channelUnregistered(ctx);
            System.out.println("连接关闭。。。");
        }
    }
}
