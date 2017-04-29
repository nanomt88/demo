package demo.netty.heartbeat;

import demo.netty.autoconnection.RequestObject;
import demo.netty.autoconnection.ResponseObject;
import demo.netty.util.MD5;
import demo.netty.util.MarshallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZBOOK-17 on 2017/4/19.
 *
 *      服务端，接受客户端定时发送过来的心跳信息
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
                    .childOption(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置Marshalling编解码器
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new ReadTimeoutHandler(5));   //五秒内无数据，自动断开连接
                            ch.pipeline().addLast(new HeartbeatHandle());
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
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    private static class HeartbeatHandle extends SimpleChannelInboundHandler {

        /**
         * 服务端保存客户端 认证的信息，进行认证
         *  key: ip地址
         *  value : 认证的信息
         */
        private static Map<String,String> AUTH_IP_MAP = new HashMap<>();


        static {
            //在实际情况中，以下内容应该是从配置文件（或者数据库）中读取
            AUTH_IP_MAP.put("127.0.0.1","FROM_LOCALHOST");
            AUTH_IP_MAP.put("localhost","FROM_LOCALHOST");
        }

        private static String SUCCESS_KEY = "LOGIN_SUCCEED";

        private void auth(ChannelHandlerContext ctx, String msg){
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = address.getHostName();
            if(!AUTH_IP_MAP.containsKey(ip.toLowerCase())){
                //认证失败，则关闭连接
                ctx.writeAndFlush("connect failed...").addListener(ChannelFutureListener.CLOSE);
                return;
            }
            String key = AUTH_IP_MAP.get(ip);


            //格式 时间戳,sign ，使用md5加密
            String[] keys = msg.split(",");
            Long time = Long.parseLong(keys[0]);

            //时间误差在20秒内
            if(keys.length<2 || ( System.currentTimeMillis() - time > 1000*10 || time - System.currentTimeMillis() > 1000*10)){
                ctx.writeAndFlush("connect failed...").addListener(ChannelFutureListener.CLOSE);
                return;
            }
            //使用 AUTH_IP_MAP 中的key 进行MD5验签
            if(!MD5.verify(keys[0],keys[1], key, "UTF-8")){
                //认证失败，则关闭连接
                ctx.writeAndFlush("connect failed...").addListener(ChannelFutureListener.CLOSE);
            }
            ctx.writeAndFlush(SUCCESS_KEY);
        }

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
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(msg instanceof String){
                auth(ctx, (String) msg);

            } else if(msg instanceof ServerInfo){
                ServerInfo info = (ServerInfo) msg;

                System.out.println("--------------------------------------------");
                System.out.println("当前主机ip为: " + info.getIp());
                System.out.println("当前主机cpu情况: ");
                Map<String, Object> cpu = info.getCpuInfo();
                System.out.println("总使用率: " + cpu.get("combined"));
                System.out.println("用户使用率: " + cpu.get("user"));
                System.out.println("系统使用率: " + cpu.get("sys"));
                System.out.println("等待率: " + cpu.get("wait"));
                System.out.println("空闲率: " + cpu.get("idle"));

                System.out.println("当前主机memory情况: ");
                Map<String, Object> memory = info.getMemoryInfo();
                System.out.println("内存总量(K): " + memory.get("total"));
                System.out.println("当前内存使用量(K): " + memory.get("used"));
                System.out.println("当前内存剩余量(K): " + memory.get("free"));
                System.out.println("--------------------------------------------");

                ctx.writeAndFlush("info received!");
            }else {
                //认证失败，则关闭连接
                ctx.writeAndFlush("connect failed...").addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
