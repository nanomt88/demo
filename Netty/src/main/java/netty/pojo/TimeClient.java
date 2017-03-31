package netty.pojo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.timeserver.TimeClientWithDecode;

import java.util.Date;
import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/3/31.
 */
public class TimeClient {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(new TimeEncode(), new TimeClientHandle());
            }
        });

        Channel channel = bootstrap.connect("127.0.0.1", 8080).channel();
    }
    /**
     *  继承 ChannelInboundHandlerAdapter 类，需要自己手工释放 ByteBuf
     */
    private static class TimeClientHandle extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
            //System.out.println("server send msg:"+ s);
            UnixTime time = (UnixTime) s;
            System.out.println(time);
            ctx.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.out.println("error.....");
            cause.printStackTrace();
            ctx.close();
        }
    }

    /**
     * 自定义转码 类，负责超过4个字符的时候，进行传输
     */
    static class TimeEncode extends ChannelOutboundHandlerAdapter {

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("============> write in");
            UnixTime time = (UnixTime) msg;
            ByteBuf encode = ctx.alloc().buffer(4);
            encode.writeInt((int) time.getTime());
            ctx.write(encode,promise);
            //1. ChannelPromise : 当编码后的数据写到通道的时候，netty可以通过这个对象知道是成功还是失败
            //2. 不需要在调用flush() 因为处理器：ChannelOutboundHandlerAdapter 已经单独分离出一个flush方法，需要的话可以复写flush方法
        }
    }

    static class  TimeEncodeSimple extends MessageToByteEncoder<UnixTime>{

        @Override
        protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
            out.writeInt((int) msg.getTime());
        }
    }

}
