package demo.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/29 上午10:56
 * @Description: //TODO
 */

public class Server {
    public void run(int port) {

        EventLoopGroup group = new NioEventLoopGroup();

        try{

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ServerHandler());

            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run(8888);
        server.run(8889);
    }

    private static class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        private static final String[] MSG = {
                "行尸走肉","金蝉脱壳","不吐不快","插翅难逃","珠光宝气",
                "百里挑一","金玉满堂","背水一战","霸王别姬","天上人间",
                "海阔天空","情非得已","满腹经纶","兵临城下","春暖花开",
                "黄道吉日","天下无双","偷天换日","两小无猜","卧虎藏龙"};

        private String getMsg(){
            int nextInt = ThreadLocalRandom.current().nextInt(MSG.length);
            return MSG[nextInt];
        }
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
            String req = datagramPacket.content().toString(CharsetUtil.UTF_8);
            System.out.println(req);
            if("字典查询?".equals(req)){
                ctx.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer("你要的字典:"+ getMsg() , CharsetUtil.UTF_8),
                        datagramPacket.sender()));
            }
        }
    }
}
