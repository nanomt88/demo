package netty.demo;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZBOOK-17 on 2017/2/28.
 */
public class NettyClientHello {
    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("encode",new StringEncoder());
                pipeline.addLast("handle",new ClientHandle());
                return pipeline;
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1",8888));
        Channel channel = channelFuture.getChannel();
        System.out.println("client started...");
        channel.write("hihi");
    }
}
