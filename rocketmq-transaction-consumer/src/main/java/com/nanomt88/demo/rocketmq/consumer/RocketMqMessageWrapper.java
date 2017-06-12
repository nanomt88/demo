package com.nanomt88.demo.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用封装逻辑，可以用来进行数据转型，数据入库等
 */
@Service
public class RocketMqMessageWrapper implements MessageListenerConcurrently {

    @Autowired
    private ConsumerMessageListener rocketMqMessageListener;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (rocketMqMessageListener.onMessage(msgs, context)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
