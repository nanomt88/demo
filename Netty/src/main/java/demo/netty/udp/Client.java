package demo.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/29 上午10:59
 * @Description: //TODO
 */

public class Client {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try{

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ClientHandle());

            Channel channel = bootstrap.bind(0).sync().channel();

            //向网段内所有的机器广播UDP消息
            channel.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("字典查询?" , CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255",8888))).sync();
            if(!channel.closeFuture().await(15000)){
                System.out.println("连接超时");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket msg) throws Exception {

            String response = msg.content().toString(CharsetUtil.UTF_8);
            if(response.startsWith("你要的字典")){
                System.out.println(response);
                channelHandlerContext.close();
            }
        }
    }
}
