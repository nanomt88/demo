package netty.serial;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ZBOOK-17 on 2017/4/20.
 */
public class Client {
    private static final String END_TAG = "$_$";

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
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

            for(int i=0 ; i<5; i++){
                RequestObject req = new RequestObject();
                req.setId(""+i);
                req.setName("pic"+i);
                req.setRequestMessage("我是client："+i);
                //读取文件
                String path = System.getProperty("user.dir") + File.separatorChar + "netty" +File.separatorChar +"file"
                        +File.separatorChar + "source" +  File.separatorChar + req.getName()+".jpg";
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

                //设置字节流
                req.setAttachment(bytes);
                future.channel().writeAndFlush(req);
            }

            //关闭端口和连接
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandle extends SimpleChannelInboundHandler<ResponseObject> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ResponseObject msg) throws Exception {
            System.out.println(msg);
        }
    }
}
