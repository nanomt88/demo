package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.entity.EventConsumerTask;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author nanomt88@gmail.com
 * @create 2017-06-21 7:35
 **/
public interface EventConsumerService {

    void commitMessage(MessageExt msg) throws Exception;

    EventConsumerTask getEventConsumerTask(String topic);

    void syncConsumerEventMessages() throws Exception;

    /**
     * 查询 上一此同步时间之后，未同步的消息列表
     *
     * @param topic
     * @param createTime 上一次同步到的时间
     * @return message  message_key 列表
     */
    List<String> findEventMessageByTopic(String topic, Date createTime);
}
