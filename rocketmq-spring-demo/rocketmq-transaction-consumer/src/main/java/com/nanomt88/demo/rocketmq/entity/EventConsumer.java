package com.nanomt88.demo.rocketmq.entity;


import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 生产者 消息类，用于记录发送过得消息
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-19 21:56
 **/
@Entity
@Lazy(value=false)
public class EventConsumer implements Serializable{

    private static final long serialVersionUID = -996634347904518089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * 消息主题
     */
    private String topic ;
    /**
     * 消息关键字 KEY
     */
    private String msgKey;
    /**
     * 消息体
     */
    private String msgBody;
    /**
     * 消息扩展内容
     */
    private String msgExtra;

    private Date createTime;

    private Date lastUpdateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgExtra() {
        return msgExtra;
    }

    public void setMsgExtra(String msgExtra) {
        this.msgExtra = msgExtra;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "EventConsumer{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", msgKey='" + msgKey + '\'' +
                ", msgBody='" + msgBody + '\'' +
                ", msgExtra='" + msgExtra + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
