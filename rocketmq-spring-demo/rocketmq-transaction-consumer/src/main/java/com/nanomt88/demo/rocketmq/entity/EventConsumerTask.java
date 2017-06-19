package com.nanomt88.demo.rocketmq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 *  消费者 消费消息的记录，记录每个 topic 上一次跟生产者同步的时间
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:34
 **/
@Entity
public class EventConsumerTask {
    /**
     * 消息 topic
     */
    @Id
    private String topic ;

    /**
     * 上一次同步时间
     */
    private Date updateTime;

}
