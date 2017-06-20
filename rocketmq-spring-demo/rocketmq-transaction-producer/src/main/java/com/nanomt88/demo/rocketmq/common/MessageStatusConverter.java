package com.nanomt88.demo.rocketmq.common;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/19 下午6:55
 * @Description:
 */

@Converter(autoApply = true)
public class MessageStatusConverter implements AttributeConverter<MessageStatus,Integer> {

    @Override
    public Integer convertToDatabaseColumn(MessageStatus auditType) {
        return auditType.getValue();
    }

    @Override
    public MessageStatus convertToEntityAttribute(Integer integer) {
        return MessageStatus.parse(integer);
    }
}
