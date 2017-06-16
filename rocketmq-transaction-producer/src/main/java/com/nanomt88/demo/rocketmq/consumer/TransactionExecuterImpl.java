package com.nanomt88.demo.rocketmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.entity.Pay;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by ZBOOK-17 on 2017/6/16.
 */
@Component
public class TransactionExecuterImpl implements LocalTransactionExecuter {

    @Autowired
    private PayService payService;

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        try {
            //读取数据
            JSONObject messageBody = JSON.parseObject(new String(msg.getBody(), "UTF-8"));

            String username = messageBody.getString("username");
            BigDecimal amount = new BigDecimal(messageBody.getString("amount"));
            String type = messageBody.getString("type");

            //持久化数据
            payService.updateAmountByUsername(amount, type, username);

            //成功则通知MQ消息变更，该消息变为：确认发送

            return LocalTransactionState.COMMIT_MESSAGE;

        } catch (Exception e) {

            e.printStackTrace();
            //失败则不通知MQ该消息回滚

            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

    }
}
