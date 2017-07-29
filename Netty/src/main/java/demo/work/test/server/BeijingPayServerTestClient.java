package demo.work.test.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

/**
 * @author nanomt88@gmail.com
 * @create 2017-07-29 16:11
 **/
public class BeijingPayServerTestClient {

    private static String[] STR = new String[]{"aaaaaaaa","bbbbbbbbbb","cccccccccccccccccccccccc","dddddddddddddddd","fffff","ggggggggggggggggggggggggggggggggg","mm"};

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(new StringEncoder())
                        .addLast(new StringDecoder())
                        .addLast(new TestClientHandle());
            }
        });

        Channel channel = bootstrap.connect("127.0.0.1", 8181).channel();

        System.out.println("client started...");

        Random random = new Random();
        while (true){

            try {

                StringBuffer response = new StringBuffer();
                response.append("<cstcode>").append(random.nextInt(100)+"").append("</cstcode>");
                response.append("<cstname>").append(UUID.randomUUID().toString()).append("</cstname>");
                response.append("<errmsg>").append(STR[random.nextInt(6)]).append("</errmsg>");
                response.append("<retcode>").append(random.nextInt(100)+"").append("</retcode>");
                response.append("<sacode>").append(UUID.randomUUID().toString().substring(random.nextInt(32))).append("</sacode>");
                String xmlString = new String(response.toString().getBytes(), "GB2312");

                String string = String.valueOf(String.format("%04d", xmlString.getBytes().length)) + xmlString;

                //将报文拆成两次发送，测试是否能成功接受
                channel.writeAndFlush(string.substring(0,30));

                Thread.sleep(3*1000);
                //
                channel.writeAndFlush(string.substring(30,string.length()));

                Thread.sleep(3*1000);

                //发送心跳
                channel.writeAndFlush("0000");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //group.shutdownGracefully();
    }

    private static class TestClientHandle extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.println("server send msg:"+ s);
        }
    }


    @Test
    public  void test() throws UnsupportedEncodingException {

        Random random = new Random();

        StringBuffer response = new StringBuffer();
        response.append("<cstcode>").append(random.nextInt(100)+"").append("</cstcode>");
        response.append("<cstname>").append(UUID.randomUUID().toString()).append("</cstname>");
        response.append("<errmsg>").append(STR[random.nextInt(6)]).append("</errmsg>");
        response.append("<retcode>").append(random.nextInt(100)+"").append("</retcode>");
        response.append("<sacode>").append(UUID.randomUUID().toString().substring(random.nextInt(32))).append("</sacode>");
        String xmlString = new String(response.toString().getBytes(), "GB2312");

        String string = String.valueOf(String.format("%04d", xmlString.getBytes().length)) + xmlString;
        System.out.println(string);

        System.out.println(string.substring(0,30));


        System.out.println(string.substring(30,string.length()));

    }
}
