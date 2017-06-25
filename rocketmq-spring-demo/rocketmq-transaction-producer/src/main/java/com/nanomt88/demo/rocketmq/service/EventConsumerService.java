package com.nanomt88.demo.rocketmq.service;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author nanomt88@gmail.com
 * @create 2017-06-21 7:35
 **/
public interface EventConsumerService {

    /**
     * 检查消息，把未发送的消息重新发送
     * @param msg
     * @throws Exception
     */
    void checkMessageAndResend(MessageExt msg) throws Exception ;

}
