package demo.netty.download;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/29 下午9:45
 * @Description: //TODO
 */

public class HttpDownloadServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
    private static final String DEFAULT_URL = "/file/source";

    public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {

        //配置SSL，加载证书
        final SslContext sslContext ;
        if(SSL){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }else{
            sslContext = null;
        }

        //创建线程池组
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            //创建服务器辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //加入 HTTP的解码器
                            p.addLast("http-decoder",new HttpRequestDecoder());
                            //加入ObjectAggregator解码器，作用是他会把多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                            p.addLast("http-aggregator", new HttpObjectAggregator(65535));
                            //加入 HTTP的解码器
                            p.addLast("http-encoder", new HttpResponseEncoder());
                            //加入chunked 主要作用是支持异步发送的码流（大文件），但不占用过多内存，防止java内存泄漏
                            p.addLast("http-chunked", new ChunkedWriteHandler());
                            p.addLast("fileServerHandler", new HttpFileServerHandler(DEFAULT_URL));
                        }
                    });

            //绑定端口
            ChannelFuture future = bootstrap.bind(8080).sync();
            System.out.println("HTTP文件目录服务器启动，网址是 : " + (SSL ? "https" : "http" )+"://192.168.1.200:"
                    + PORT + DEFAULT_URL);

            //等待服务器监听端口关闭
            future.channel().closeFuture().sync();

        }finally {
            //关闭线程组  释放资源 ： 必须先关闭boss，先停止接受新的连接，再关闭worker
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
}
