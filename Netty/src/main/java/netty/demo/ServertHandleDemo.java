package netty.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZBOOK-17 on 2017/2/28.
 *
 */
public class ServertHandleDemo extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " say:"+ msg);
        ctx.writeAndFlush("Received your message ! " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " \n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 当有服务端连接进来的时候执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server channelActive...");
        super.channelActive(ctx);
    }
}
