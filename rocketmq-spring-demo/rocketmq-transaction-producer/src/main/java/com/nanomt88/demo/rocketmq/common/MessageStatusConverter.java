package com.nanomt88.demo.rocketmq.common;

import com.lxft.nova.commons.type.payment.AuditTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/19 下午6:55
 * @Description:
 */

@Converter(autoApply = true)
public class MessageStatusConverter implements AttributeConverter<AuditTypeEnum,Integer> {

    @Override
    public Integer convertToDatabaseColumn(AuditTypeEnum auditType) {
        return auditType.getValue();
    }

    @Override
    public AuditTypeEnum convertToEntityAttribute(Integer integer) {
        return AuditTypeEnum.parse(integer);
    }
}
