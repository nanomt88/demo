package com.nanomt88.demo.rocketmq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.consumer.MQProducer;
import com.nanomt88.demo.rocketmq.consumer.TransactionExecuterImpl;
import com.nanomt88.demo.rocketmq.dao.EventProducerDao;
import com.nanomt88.demo.rocketmq.dao.PayDao;
import com.nanomt88.demo.rocketmq.entity.EventProducer;
import com.nanomt88.demo.rocketmq.entity.Pay;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(value = false)
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类  //  1.4.0 以后版本
//@SpringApplicationConfiguration(classes = Application.class)// 1.4.0 前版本
public class PayServiceImplTest {

    Logger logger = LoggerFactory.getLogger(PayServiceImplTest.class);

    @Autowired
    Environment env;

    @Autowired
    PayService payService;

    @Autowired
    PayDao payDao;

    @Autowired
    MQProducer mqProducer;

    @Autowired
    TransactionExecuterImpl transactionExecuter;

    @Autowired
    EventProducerDao eventProducerDao;


    @Test
    public void findEventList() {

        String[] array = new String[]{"", "", "", ""};
        List<String> keys = Arrays.asList(array);
        List<EventProducer> list = eventProducerDao.findByTopicAndStatusAndCreateTimeLessThanEqual("payTopic",
                MessageStatus.PREPARED, new Date(0));

        logger.info("array : {}", list);
        assertNotNull(list);

        eventProducerDao.updatePreparedEventByTopicAndMsgKeyIn(MessageStatus.SUBMITTED, "payTopic", keys);

        list = eventProducerDao.findByTopicAndStatusAndCreateTimeLessThanEqual("payTopic",
                MessageStatus.PREPARED, new Date(0));

        assertEquals(list.size(), 0);
    }

    @Test
    public void updateAmountByUsername() throws Exception {
        String username = "张三";

        Pay pay = payDao.getOne(1L);
        BigDecimal amount = pay.getAmount();

        payService.updateAmountByUsername(new BigDecimal(100), "IN", username);

        Pay pay2 = payDao.getOne(1L);

        assertEquals(amount.add(new BigDecimal(100)), pay2.getAmount());

    }

    @Test
    public void commitMessage() {
        EventProducer event = new EventProducer();
        event.setTopic(env.getProperty("rocketmq.topic"));

        String uuid = UUID.randomUUID().toString();
        event.setMsgKey(uuid);
        event.setMsgBody("TEST1");
        event.setStatus(MessageStatus.PREPARED);
        event.setCreateTime(new Date());
        event = eventProducerDao.saveAndFlush(event);

        org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message();
        msg.setTopic(env.getProperty("rocketmq.topic"));
        msg.setKeys(uuid);
        payService.commitMessage(msg);

        EventProducer newObj = eventProducerDao.findByTopicAndMsgKey(event.getTopic(), event.getMsgKey());

        assertEquals(newObj.getStatus(), MessageStatus.SUBMITTED);

        payService.rollBackMessage(msg);

        newObj = eventProducerDao.findByTopicAndMsgKey(event.getTopic(), event.getMsgKey());

        assertEquals(newObj.getStatus(), MessageStatus.ROLL_BACK);
    }

    @Test
    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public void sendMessage() throws UnsupportedEncodingException, MQClientException {

        System.out.println("=================启动===================");
        long time = System.currentTimeMillis();
        for (int j = 0; j < 500; j++) {

            sendNewMessage();

            System.out.println("===================> 完成： " + j * 30 / 15000 + "%");
        }
        System.out.println("=================消息发送完成=================== 耗时："+ (System.currentTimeMillis() - time));
        while (true){
            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void sendNewMessage()throws UnsupportedEncodingException, MQClientException{
        for (int i = 0; i < 10; i++) {

            //构造消息
            sendMessage("张三","IN",  "OUT");
            sendMessage("李四","IN",  "OUT");
            sendMessage("王五","OUT", "IN");

        }
    }


    private void sendMessage(String name, String type1, String type2) throws UnsupportedEncodingException, MQClientException {

        Message message = new Message();
        message.setTopic(env.getProperty("rocketmq.topic"));
        message.setTags("tag");
        //key
        String uuid = UUID.randomUUID().toString();
        message.setKeys(uuid);
        System.out.println("message ID : " + uuid);

        JSONObject json = new JSONObject();
        json.put("username", name);
        json.put("amount", "1");
        json.put("payType", type1);
        json.put("balanceMode", type2);

        message.setBody(json.toJSONString().getBytes("UTF-8"));

        //可以追加参数
        Map<String, Object> map = new HashMap<>();

        //记录发送消息到日志表
        EventProducer event = new EventProducer();
        event.setTopic(message.getTopic());
        event.setMsgKey(message.getKeys());
        event.setMsgBody(new String(message.getBody(), "UTF-8"));
        event.setMsgExtra(JSONObject.toJSONString(map));
        event.setStatus(MessageStatus.PREPARED);
        event.setCreateTime(new Date());
        event = eventProducerDao.save(event);

        message.putUserProperty("id", String.valueOf(event.getId()));

        //发送消息
        mqProducer.sendTransactionMessage(message, transactionExecuter, map);
    }

    @Test
    public void queryMessage() throws Exception {
        String key = "dfb9425c-e107-4b2b-9425-2cadcc48a849";
        long end = new Date().getTime();
        long begin = end - 60 * 1000 * 60 * 24;
        QueryResult queryResult = mqProducer.queryMessage(env.getProperty("rocketmq.topic"), key, 10, begin, end);

        List<MessageExt> list = queryResult.getMessageList();

        for (MessageExt msg : list) {
            System.out.println("===================================");
            Map<String, String> m = msg.getProperties();
            System.out.println("keys:" + m.keySet().toString());
            System.out.println("values:" + m.values().toString());
            System.out.println("message:" + msg.toString());
            System.out.println("==-->内容: " + new String(msg.getBody(), "utf-8"));
            System.out.println("Prepared :" + msg.getPreparedTransactionOffset());
            LocalTransactionState ls = this.mqProducer.check(msg);
            System.out.println("结果：" + ls);
        }

    }
}