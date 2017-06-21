package com.nanomt88.demo.rocketmq.service.impl;


import com.alibaba.fastjson.JSON;
import com.nanomt88.demo.rocketmq.dao.BalanceDao;
import com.nanomt88.demo.rocketmq.dao.EventConsumerDao;
import com.nanomt88.demo.rocketmq.dao.EventConsumerTaskDao;
import com.nanomt88.demo.rocketmq.entity.EventConsumer;
import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import com.nanomt88.demo.rocketmq.service.EventConsumerService;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName EventConsumerServiceImpl
 * @author
 * @date 2016年11月10日
 */
@Service
@Transactional
public class EventConsumerServiceImpl implements EventConsumerService {

	@Autowired
	private EventConsumerDao eventConsumerDao;

	@Autowired
    private EventConsumerTaskDao eventConsumerTaskDao;

	@Override
	public void commitMessage(MessageExt msg) throws Exception {

        EventConsumer event = new EventConsumer();
		event.setTopic(msg.getTopic());
		event.setMsgKey(msg.getKeys());
		event.setMsgBody(new String(msg.getBody(),"UTF-8"));
		if(msg.getProperties()!=null) {
			event.setMsgExtra(JSON.toJSONString(msg.getProperties()));
		}
		event.setCreateTime(new Date());
		eventConsumerDao.save(event);
	}

    @Override
    public EventConsumerTask getEventConsumerTask(String topic) {
        return eventConsumerTaskDao.getOne(topic);
    }

    @Override
    public void syncConsumerEventMessages(EventConsumerTask task) {
        List<Long> idList = findEventMessageByTopic(task.getTopic(), task.getUpdateTime());


        eventConsumerTaskDao.save(task);
    }

    @Override
    public List<Long> findEventMessageByTopic(String topic, Date createTime) {
        return eventConsumerDao.findIdsByTopicAndCreateTimeAfter(topic, createTime);
    }

}
