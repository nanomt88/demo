package com.nanomt88.demo.rocketmq.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.common.SyncTaskInfoDTO;
import com.nanomt88.demo.rocketmq.consumer.MQProducer;
import com.nanomt88.demo.rocketmq.consumer.TransactionExecuterImpl;
import com.nanomt88.demo.rocketmq.dao.EventProducerDao;
import com.nanomt88.demo.rocketmq.dao.EventProducerTaskDao;
import com.nanomt88.demo.rocketmq.entity.EventProducer;
import com.nanomt88.demo.rocketmq.entity.EventProducerTask;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
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
    EventProducerDao eventProducerDao;

    @Autowired
    EventProducerTaskDao eventProducerTaskDao;

    @Autowired
    MQProducer mqProducer;

    @Autowired
    TransactionExecuterImpl transactionExecuter;

    @Autowired
    private Environment env;

    @Override
    public void checkMessageAndResend(MessageExt msg) throws Exception {

        String json = new String(msg.getBody(), "UTF-8");
        if (StringUtils.isBlank(json)) {
            return;
        }

        SyncTaskInfoDTO dto = JSONObject.parseObject(json, SyncTaskInfoDTO.class);
        if (dto == null || dto.getUpdateTime() == null || StringUtils.isBlank(dto.getTopic())) {
            return;
        }

        //读取延时配置，防止刚刚提交的消息又被查询出来进行补发，导致消息重复
        String  delayConfig = env.getProperty("rocketmq.sync.task.delayTime");
        Date delayTime;
        if(StringUtils.isBlank(delayConfig)){
            delayTime = dto.getUpdateTime();
        }else {
            long delay = 0 ;
            try {
                delay = Long.parseLong(delayConfig);
            }catch (NumberFormatException e){
                logger.warn("加载配置rocketmq.sync.task.delayTime[{}]值错误，忽略此配置", delayConfig);
            }
            delayTime = new Date(dto.getUpdateTime().getTime() - 1000 * delay);
        }

        //获取上一次同步的位置
        EventProducerTask task = eventProducerTaskDao.findOne(env.getProperty("rocketmq.sync.task.topic"));
        if(task == null){
            task = new EventProducerTask(env.getProperty("rocketmq.sync.task.topic"), new Date(0));
        }

        //读取表中状态还是为PREPARED：未发送的消息，进行重发
        List<EventProducer> messages = eventProducerDao.findByTopicAndStatusAndCreateTimeLessThanEqual(dto.getTopic(),
                MessageStatus.PREPARED, delayTime);
        if(messages == null || messages.isEmpty()){
            return;
        }
        for (EventProducer eventProducer : messages) {
            sendMessage( eventProducer);
        }
        //设置本次同步位置，下一次从这开始同步
        task.setUpdateTime(delayTime);
        eventProducerTaskDao.save(task);
    }

    /**
     * 补发消息
     *
     * @param event
     * @throws Exception
     */
    private void sendMessage(EventProducer event) throws Exception {
        //构造消息
        Message message = new Message();
        message.setTopic(event.getTopic());
        message.setTags("tag");
        //key
        message.setKeys(event.getMsgKey());
        message.setBody(event.getMsgBody().getBytes("UTF-8"));
        //可以追加参数
        Map<String, Object> map = JSONObject.parseObject(event.getMsgExtra(), Map.class);
        //发送消息
        mqProducer.sendTransactionMessage(message, transactionExecuter, map);
        logger.info("补发消息： key[{}], message[{}]", event.getMsgKey(), event.getMsgBody());
    }
}
