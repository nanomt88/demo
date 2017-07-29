package demo.work.client;

import demo.work.JaxbUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *  给北京支付发送查询请求，对发送报文encode
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-29 10:21
 **/
public class XmlEncode extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msg == null){
            return;
        }
        //如果是字符串：直接发送（ 需要定期发送心跳信息，心跳就是字符串 0000 ）
        if(msg instanceof String){
            out.writeBytes(((String) msg).getBytes());
            return;
        }
        byte[] body = convertToBytes(msg);  //将对象转换为byte，伪代码，具体用什么进行序列化，你们自行选择。可以使用我上面说的一些
        int dataLength = body.length;  //读取消息的长度
        out.writeBytes(String.format("%04d", dataLength).getBytes());  //先将消息长度写入，也就是消息头
        out.writeBytes(body);  //消息体中包含我们要发送的数据
    }

    /**
     * 转换发送数据
     * @param msg
     * @return
     */
    private byte[] convertToBytes(Object msg) {
        String xmlString = JaxbUtil.convertToXml(msg);
        return String.valueOf(xmlString).getBytes();
    }
}
