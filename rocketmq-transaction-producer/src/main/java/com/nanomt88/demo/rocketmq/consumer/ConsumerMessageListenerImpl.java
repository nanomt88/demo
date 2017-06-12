package com.nanomt88.demo.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/6/11.
 */
@Service
public class ConsumerMessageListenerImpl implements ConsumerMessageListener {

    Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

    @Override
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) {

        for(MessageExt msg : messages) {

            logger.info("收到消息：{}, msg：{}", msg.getKeys(), messages);

        }
        return true;
    }
}
