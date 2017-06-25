package com.nanomt88.demo.rocketmq.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.common.SyncTaskInfoDTO;
import com.nanomt88.demo.rocketmq.consumer.MQProducer;
import com.nanomt88.demo.rocketmq.dao.EventConsumerDao;
import com.nanomt88.demo.rocketmq.dao.EventConsumerTaskDao;
import com.nanomt88.demo.rocketmq.entity.EventConsumer;
import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author
 * @ClassName EventConsumerServiceImpl
 * @date 2016年11月10日
 */
@Service
@Transactional
public class EventConsumerServiceImpl implements EventConsumerService {

    Logger logger = LoggerFactory.getLogger(EventConsumerServiceImpl.class);

    @Autowired
    private EventConsumerDao eventConsumerDao;

    @Autowired
    private EventConsumerTaskDao eventConsumerTaskDao;

    @Autowired
    private MQProducer producer;

    @Autowired
    private Environment env;

    @Override
    public void commitMessage(MessageExt msg) throws Exception {

        EventConsumer event = new EventConsumer();
        event.setTopic(msg.getTopic());
        event.setMsgKey(msg.getKeys());
        event.setMsgBody(new String(msg.getBody(), "UTF-8"));
        if (msg.getProperties() != null && !msg.getProperties().isEmpty()) {
            event.setMsgExtra(JSON.toJSONString(msg.getProperties()));
        }
        event.setCreateTime(new Date());
        eventConsumerDao.save(event);
    }

    @Override
    public EventConsumerTask getEventConsumerTask(String topic) {
        return eventConsumerTaskDao.findOne(topic);
    }

    @Override
    public void syncConsumerEventMessages() throws Exception {
        //获取当前时间
        Date now = new Date();

        EventConsumerTask task = getEventConsumerTask(env.getProperty("rocketmq.topic"));
        if(task == null){
            task = new EventConsumerTask();
            task.setTopic(env.getProperty("rocketmq.topic"));
            task.setUpdateTime(new Date(0));
        }
        logger.info("同步消息任务运行至 ： task [{}]", task);


        //从上一次抽取的时间 之后进行抽取
//        List<String> count = findEventMessageByTopic(task.getTopic(), task.getUpdateTime());
        /**
         * 先发送消息，发送消息是同步过程，消息发送成功之后再入库
         */
        //Set<String> messageKeys = new HashSet<String>((count.size() * 4 / 3) + 1);
        //messageKeys.addAll(count);
//        SyncTaskInfoDTO dto = new SyncTaskInfoDTO(task.getTopic(), now, messageKeys);
        SyncTaskInfoDTO dto = new SyncTaskInfoDTO(task.getTopic(), now, null);

        Message message = new Message();
        message.setTopic(env.getProperty("rocketmq.sync.task.topic"));
//        message.setTags("tag");
        message.setKeys(UUID.randomUUID().toString());
        message.setBody(JSONObject.toJSONBytes(dto));
        producer.sendMessage(message);

        //将抽取的时间记录到数据库，然后下一次从这个时间之后进行抽取
        task.setUpdateTime(now);
        eventConsumerTaskDao.save(task);
    }

    @Override
    public List<String> findEventMessageByTopic(String topic, Date createTime) {
        return eventConsumerDao.findMsgKeyByTopicAndCreateTimeGreaterThan(topic, createTime);
    }

}
