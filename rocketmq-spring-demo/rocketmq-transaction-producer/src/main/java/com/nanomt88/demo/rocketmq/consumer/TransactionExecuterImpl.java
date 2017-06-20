package com.nanomt88.demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 用于 producer发送事物消息之后，确认消息是否成功的类
 */
@Component
public class TransactionExecuterImpl implements LocalTransactionExecuter {

    private static final Logger logger = LoggerFactory.getLogger(TransactionExecuterImpl.class);

    @Autowired
    private PayService payService;

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        try {
            //读取数据
            JSONObject messageBody = JSON.parseObject(new String(msg.getBody(), "UTF-8"));

            String username = messageBody.getString("username");
            BigDecimal amount = new BigDecimal(messageBody.getString("amount"));
            String type = messageBody.getString("payType");

            //持久化数据
            payService.updateAmountByUsername(amount, type, username, msg);

            //记录消息日志
            payService.commitMessage(msg);

            //成功则通知MQ消息变更，该消息变为：确认发送
            logger.info("确认提交消息，key：{}", msg.getKeys());

            return LocalTransactionState.COMMIT_MESSAGE;

        } catch (Exception e) {

            e.printStackTrace();
            //失败则不通知MQ该消息回滚

            //回滚消息日志
            payService.rollBackMessage(msg);

            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

    }
}
