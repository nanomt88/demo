package netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * Created by ZBOOK-17 on 2017/4/21.
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {

    WebSocketServerHandshaker handshaker ;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统的HTTP请求
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        }
        // web socket接入
        else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的指令
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        //判断是否是ping消息
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        }

        //本示例只支持文本消息，不支持二进制消息
        if( !(frame instanceof TextWebSocketFrame) ){
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        //返回应答消息
        String reqText = ((TextWebSocketFrame) frame).text();
        System.out.println(String.format("%s received %s", ctx.channel(), reqText));
        ctx.channel().write(new TextWebSocketFrame(reqText + ", 你好。。。，现在时间："+ new java.util.Date().toString()));

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //如果HTTP解码失败，返回HTTP异常
        //HTTP请求头参数Upgrade 必须为 websocket
        if(!req.decoderResult().isSuccess() || !"websocket".equals(req.headers().get("Upgrade"))){
            sendHttResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory factory =
                new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null,false);
        handshaker = factory.newHandshaker(req);

        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), req);
        }


    }

    private void sendHttResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse response) {
        //返回应答给客户端
        if(response.status().code() != 200 ){
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response, response.content().readableBytes());
        }
        //如果是非Keep-Alive，关闭连接
        ChannelFuture future = ctx.writeAndFlush(response);
        if(!HttpUtil.isKeepAlive(req) || response.status().code() != 200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
