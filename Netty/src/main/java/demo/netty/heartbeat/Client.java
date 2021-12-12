package demo.netty.heartbeat;

import demo.netty.serial.MarshallingCodeFactory;
import demo.netty.serial.ResponseObject;
import demo.netty.util.MD5;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/29 下午5:31
 * @Description: 周期向服务端报告心跳信息
 *          运行之前 需要设置java.library.path
 *          -Djava.library.path=/Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home/lib
 */

public class Client {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                            pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            pipeline.addLast(new ClientHandle());
                        }
                    });


            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();

            //关闭端口和连接
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<Object> {
        //MD5签名密钥
        private static String KEY = "FROM_LOCALHOST";

        //返回的成功标识
        private static String SUCCESS_KEY = "LOGIN_SUCCEED";

        /**
         * 创建一个定时任务 线程池
         */
        private ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        //本机IP
        private static InetAddress address;

        /**
         * 连接建立之后，发送认证信息
         *
         * @param ctx
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();

            //使用当前时间进行签名
            String time = System.currentTimeMillis() + "";
            String sign = MD5.sign(time, KEY, "UTF-8");

            //向服务器端发送签名信息
            ctx.writeAndFlush(time + "," + sign);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            //判断是否认证成功
            if (msg instanceof String) {
                String res = (String) msg;

                if (SUCCESS_KEY.equals(res)) {
                    //认证成功则，添加发送心跳的任务
                    scheduled.scheduleWithFixedDelay(new HeartbeatTask(ctx), 0, 3, TimeUnit.SECONDS);
                    System.out.println("===============与服务器认证成功===============");
                } else {
                    System.out.println("收到服务端消息：：：：：：" + msg);
                }
            }

        }

        /**
         * 发送 服务器信息的任务类
         */
        private class HeartbeatTask implements Runnable {

            private ChannelHandlerContext ctx;

            public HeartbeatTask(ChannelHandlerContext ctx) {
                this.ctx = ctx;
            }

            @Override
            public void run() {

                try {

                    ServerInfo info = new ServerInfo();
                    info.setIp(address.getHostAddress());

                    Sigar sigar = new Sigar();

                    //cpu info
                    CpuPerc cpuPerc = sigar.getCpuPerc();
                    Map<String,Object> cpuMap = new HashMap<>();
                    cpuMap.put("combined", cpuPerc.getCombined());
                    cpuMap.put("user", cpuPerc.getUser());
                    cpuMap.put("sys", cpuPerc.getSys());
                    cpuMap.put("wait", cpuPerc.getWait());
                    cpuMap.put("idle", cpuPerc.getIdle());

                    //memory info
                    Mem mem = sigar.getMem();
                    Map<String,Object> memoryMap = new HashMap<>();
                    memoryMap.put("total", mem.getTotal() / 1024L);
                    memoryMap.put("used", mem.getUsed() / 1024L);
                    memoryMap.put("free", mem.getFree() / 1024L);

                    info.setCpuInfo(cpuMap);
                    info.setMemoryInfo(memoryMap);

                    ctx.writeAndFlush(info);

                } catch (SigarException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
