package com.nanomt88.demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.dao.EventProducerDao;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ZBOOK-17 on 2017/6/11.
 */
@Service
@Transactional
public class ConsumerMessageListenerImpl implements ConsumerMessageListener {

    Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

    @Autowired
    EventConsumerService eventConsumerService;

    @Override
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) throws Exception {

        for (MessageExt msg : messages) {

            //logger.info("收到消息：{}, msg：{}", msg.getKeys(), messages);
            if (!handler(msg)) {
                return false;
            }
        }
        return true;
    }

    private boolean handler(MessageExt msg) throws Exception {
        try {

            eventConsumerService.checkMessageAndResend(msg);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }
}
