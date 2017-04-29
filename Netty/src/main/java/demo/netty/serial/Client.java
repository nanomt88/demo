package demo.netty.serial;

import demo.netty.util.GzipUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ZBOOK-17 on 2017/4/20.
 */
public class Client {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
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



            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
            System.out.println("client started...");
            for(int i=0 ; i<5; i++){
                RequestObject req = new RequestObject();
                req.setId(""+i);
                req.setName("pic"+i);
                req.setRequestMessage("我是client："+i);
                //读取文件
                String path = System.getProperty("user.dir") + File.separatorChar + "Netty" +File.separatorChar +"file"
                        +File.separatorChar + "source" +  File.separatorChar + i+".jpg";
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

                //设置字节流
                req.setAttachment(GzipUtils.gzip(bytes));
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
        } catch (Exception e) {
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
