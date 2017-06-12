package com.nanomt88.demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.nanomt88.demo.rocketmq.dao.BalanceDao;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ZBOOK-17 on 2017/6/11.
 */
@Service
public class ConsumerMessageListenerImpl implements ConsumerMessageListener {

    Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

    @Autowired
    private BalanceService balanceService;

    @Override
    public boolean onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext Context) {

        for (MessageExt msg : messages) {

            //logger.info("收到消息：{}, msg：{}", msg.getKeys(), messages);
            if (!handler(msg)) {
                return false;
            }
        }
        return true;
    }

    private boolean handler(MessageExt msg) {
        try {

            String msgBody = new String(msg.getBody(), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(msgBody);
            assert jsonObject != null && !jsonObject.isEmpty();

            String keys = msg.getKeys();
            logger.info("balance服务收到消息, keys : {}, body : {}", keys, msgBody);

            String username = jsonObject.getString("username");
            BigDecimal amount = new BigDecimal(jsonObject.getDouble("amount"));
            String mode = jsonObject.getString("balanceMode");

            balanceService.updateAmountByUsername(amount, mode, username);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
