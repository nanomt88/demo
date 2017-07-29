package demo.work.server;

import demo.work.ChargeQueryResponse;
import demo.work.JaxbUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *  解析北京支付返回的 查询报文信息
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-29 10:38
 **/
public class XmlDecoder extends ByteToMessageDecoder {
    private static final int HEAD_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) {  //这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
            return;
        }
        in.markReaderIndex();                  //我们标记一下当前的readIndex的位置
        byte[] headBytes = new byte[4];
        in.readBytes(headBytes);

        String headStr = new String(headBytes);
        if (!StringUtils.isNumeric(headStr)) {
            in.resetReaderIndex();
            return;
        }

        int dataLength = Integer.valueOf(headStr);
        if (dataLength <= 0) { // 我们读到的消息体长度为0，这是心跳信息，直接跳过。
            return;
        }

        if (in.readableBytes() < dataLength) { //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];  //  嗯，这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~~
        in.readBytes(body);  //
        ChargeQueryResponse o = convertToObject(body);  //将byte数据转化为我们需要的对象。
        if(o != null ) {
            out.add(o);
        }
    }

    private ChargeQueryResponse convertToObject(byte[] body) {
        ChargeQueryResponse response = null;
        String msg = null;
        try {
            msg = new String(body, "GB2312");

            System.out.println("原始报文：" + msg);

            if(msg.contains("<action>login</action>")){
                return null;
            }

            String root = "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\"?><root>" + StringUtils.substring(msg, msg.indexOf("<"), msg.lastIndexOf(">") + 1) + "</root>";
            response = JaxbUtil.convertToJavaBean(root.trim(), ChargeQueryResponse.class);

        } catch (UnsupportedEncodingException e) {
            System.out.println("原始报文 解析出错：" + msg);
            e.printStackTrace();
        }
        return response;
    }
}
