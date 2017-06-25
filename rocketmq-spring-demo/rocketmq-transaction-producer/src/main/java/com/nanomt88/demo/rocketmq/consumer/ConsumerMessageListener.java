package com.nanomt88.demo.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/6/11.
 */
public interface ConsumerMessageListener {

    /**
     * 消息消费接口
     * @param messages
     * @param Context
     * @return
     */
    boolean onMessage(List<MessageExt> messages,
                      ConsumeConcurrentlyContext Context) throws Exception;
}
