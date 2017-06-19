package com.nanomt88.demo.rocketmq.common;

import java.util.HashMap;
import java.util.Map;

/**
 *   生产端 发送的消息状态：
 *      0 ： PREPARED ，准备阶段，已经提交到MQ
 *      1 ：SUBMITTED ，确认提交
 * @author nanomt88@gmail.com
 * @create 2017-06-19 22:04
 **/
public enum MessageStatus {
    /**
     * 准备阶段
     */
    PREPARED (0),
    /**
     * 确认提交
     */
    SUBMITTED (1);

    private int status ;

    MessageStatus(int status){
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    private static Map<Integer, MessageStatus> mapping = new HashMap<Integer, MessageStatus>();

    static {
        for (MessageStatus at : MessageStatus.values()) {
            mapping.put(at.getValue(), at);
        }
    }

    public static MessageStatus parse(Integer val) {
        return mapping.get(val);
    }
}
