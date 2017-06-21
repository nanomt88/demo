package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;

/**
 * @author nanomt88@gmail.com
 * @create 2017-06-21 7:35
 **/
public interface EventConsumerService {

    void commitMessage(MessageExt msg) throws Exception ;

    EventConsumerTask getEventConsumerTask(String topic);

    void syncConsumerEventMessages(EventConsumerTask task);

    /**
     * 查询 上一此同步时间之后，未同步的消息列表
     * @param topic
     * @param createTime
     * @return  message  id列表
     */
    List<Long> findEventMessageByTopic(String topic , Date createTime);
}
