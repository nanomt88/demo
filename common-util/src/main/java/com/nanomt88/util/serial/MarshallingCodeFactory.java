package com.nanomt88.util.serial;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Created by ZBOOK-17 on 2017/4/25.
 */
public class MarshallingCodeFactory {
    /**
     * 创建Jboss Marshalling解码器 MarshallingDecoder
     * @return
     */
    public static MarshallingDecoder buildMarshallingDecoder(){
        //首先通过marshalling工具类创建Marshalling实例对象，参数serial标识创建的是java序列化工厂对象。
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        //创建了MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        //根据MarshallerFactory和MarshallingConfiguration创建provider
        DefaultUnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);
        //构建netty的MarshallingDecode对象，两个参数分别为provider和单个消息序列化之后的最大长度
        MarshallingDecoder marshallingDecoder = new MarshallingDecoder(provider, 1024 * 1024 * 1);
        return marshallingDecoder;
    }

    /**
     * 创建Jboss Marshalling解码器 MarshallingEncoder
     * @return
     */
    public static MarshallingEncoder buildMarshallingEncoder(){
        //首先通过marshalling工具类创建Marshalling实例对象，参数serial标识创建的是java序列化工厂对象。
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        //创建了MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        //根据MarshallerFactory和MarshallingConfiguration创建provider
        MarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);
        //构建netty的MarshallingDecode对象，两个参数分别为provider和单个消息序列化之后的最大长度
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

}
