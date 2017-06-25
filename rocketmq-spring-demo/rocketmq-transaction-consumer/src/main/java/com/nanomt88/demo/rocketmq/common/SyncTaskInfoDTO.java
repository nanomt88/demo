package com.nanomt88.demo.rocketmq.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *  consumer 和 producer 之前同步任务的DTO类
 * @author nanomt88@gmail.com
 * @create 2017-06-24 23:36
 **/
public class SyncTaskInfoDTO implements Serializable{

    private static final long serialVersionUID = -1939478379787634834L;

    private String topic ;

    private Date updateTime;

    private Set<String> messageKeys;

    public SyncTaskInfoDTO() {
    }

    public SyncTaskInfoDTO(String topic, Date updateTime, Set<String> messageKeys) {
        this.topic = topic;
        this.updateTime = updateTime;
        this.messageKeys = messageKeys;
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

    public Set<String> getMessageKeys() {
        return messageKeys;
    }

    public void setMessageKeys(Set<String> messageKeys) {
        this.messageKeys = messageKeys;
    }

    @Override
    public String toString() {
        return "SyncTaskInfoDTO{" +
                "topic='" + topic + '\'' +
                ", updateTime=" + updateTime +
                ", messageKeys=" + messageKeys +
                '}';
    }
}
