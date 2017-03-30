package netty.timeserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/3/29.
 */
public class TimeClientWithDecode {

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

        //group.shutdownGracefully();
    }

    /**
     *  继承 ChannelInboundHandlerAdapter 类，需要自己手工释放 ByteBuf
     */
    private static class TimeClientHandle extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
            //System.out.println("server send msg:"+ s);
            ByteBuf buf = (ByteBuf) s;
            try {
                long time = buf.readUnsignedInt();
                System.out.println(time);
                long currentTimeMillis = (time - 2208988800L) * 1000L;
                System.out.println("==========>" + new Date(currentTimeMillis));
                ctx.close();
            }finally {
                buf.release();
            }

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
    static class TimeEncode extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            //小于四个字节 则不读取
            if(byteBuf.readableBytes()<4){
                return;
            }
            //长度够四个字节则读取
            list.add(byteBuf.readBytes(4));
        }
    }
}
