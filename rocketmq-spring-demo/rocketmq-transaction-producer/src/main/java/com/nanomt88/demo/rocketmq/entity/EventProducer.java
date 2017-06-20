package com.nanomt88.demo.rocketmq.entity;/**
 * Created by ZBOOK-17 on 2017/6/19.
 */

import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.common.MessageStatusConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 生产者 消息类，用于记录发送过得消息
 *
 * @author nanomt88@gmail.com
 * @create 2017-06-19 21:56
 **/
@Entity
public class EventProducer implements Serializable{

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

    private String msgBody;
    /**
     * 消息扩展内容
     */
    private String msgExtra;
    /**
     * 消息状态 ， 0 ： PREPARED；1：SUBMITTED ； 2：ROLL_BACK
     */
    @Convert(converter = MessageStatusConverter.class)
    private MessageStatus status;

    private Date createTime;

    @Column(insertable = false, updatable = false)
    private Date updateTime;

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

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
