package org.tcdd.netty.hello;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by ZBOOK-17 on 2017/2/28.
 */
public class ServerHandle extends ChannelHandlerAdapter  {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
