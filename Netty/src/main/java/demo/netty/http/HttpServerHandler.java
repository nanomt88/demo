package demo.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/29 下午9:59
 * @Description: //TODO
 */

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private static final String CONTENT = "hello world, this is a http demo";


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            if(HttpUtil.is100ContinueExpected(request)){
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(CONTENT.getBytes()));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

            if(!keepAlive){ //如果不保持回话 则关闭连接
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            }else{
                response.headers().set(HttpHeaderNames.CONNECTION , HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
