package com.nanomt88.demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
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
    private BalanceService balanceService;
    @Autowired
    private EventConsumerService eventConsumerService;

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

            String msgBody = new String(msg.getBody(), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(msgBody);
            assert jsonObject != null && !jsonObject.isEmpty();

            String keys = msg.getKeys();
            logger.info("balance服务收到消息, keys : {}, body : {}", keys, msgBody);

            String username = jsonObject.getString("username");
            BigDecimal amount = new BigDecimal(jsonObject.getDouble("amount"));
            String mode = jsonObject.getString("balanceMode");

            //处理业务逻辑
            balanceService.updateAmountByUsername(amount, mode, username);

            eventConsumerService.commitMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }
}
