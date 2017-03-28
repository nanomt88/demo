package org.tcdd.netty.demo;

import org.jboss.netty.channel.*;

/**
 * Created by ZBOOK-17 on 2017/2/28.
 */
public class ServerHandle extends SimpleChannelHandler {

    /**
     * 收到消息时执行
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        String msg = (String) e.getMessage();
        String msg = (String) e.getMessage();
        System.out.println("messageReceived:"+msg);
        ctx.getChannel().write("this is ok..\n");
    }

    /**
     * 出现异常时显示
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        Throwable throwable =  e.getCause();
        System.out.println(throwable.getMessage());
    }

    /**
     * 渠道socket连接建立的时候
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");
    }

    /**
     * 连接关闭的时候，只有当连接建立之后才会调用
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
    }

    /**
     * 连接释放，释放资源。无论连接是否成功，都有调用
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
    }
}
