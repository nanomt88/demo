package com.nanomt88.demo.rocketmq.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 *  消费者 消费消息的记录，记录每个 topic 上一次跟生产者同步的时间
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:34
 **/
@Entity
public class EventProducerTask implements Serializable{

    private static final long serialVersionUID = -4375116574089716081L;
    /**
     * 消息 topic
     */
    @Id
    private String topic ;

    /**
     * 上一次同步的ID值
     */
    private Date updateTime;

    public EventProducerTask(){}

    public EventProducerTask(String topic, Date updateTime) {
        this.topic = topic;
        this.updateTime = updateTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "EventConsumerTask{" +
                "topic='" + topic + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
