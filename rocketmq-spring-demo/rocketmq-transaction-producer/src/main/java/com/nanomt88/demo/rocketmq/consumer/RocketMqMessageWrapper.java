package com.nanomt88.demo.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用封装逻辑，可以用来进行数据转型，数据入库等
 */
@Service
public class RocketMqMessageWrapper implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(RocketMqMessageWrapper.class);

    @Autowired
    private ConsumerMessageListener rocketMqMessageListener;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            if (rocketMqMessageListener.onMessage(msgs, context)) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        } catch (Exception e) {
            logger.error("Message consumer error , message : {} , error : {}", msgs, e.getMessage());
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
