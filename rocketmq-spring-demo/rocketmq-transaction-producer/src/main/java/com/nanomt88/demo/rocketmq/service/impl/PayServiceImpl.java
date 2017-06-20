package com.nanomt88.demo.rocketmq.service.impl;


import com.nanomt88.demo.rocketmq.common.MessageStatus;
import com.nanomt88.demo.rocketmq.dao.EventProducerDao;
import com.nanomt88.demo.rocketmq.dao.PayDao;
import com.nanomt88.demo.rocketmq.entity.EventProducer;
import com.nanomt88.demo.rocketmq.entity.Pay;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @ClassName UserServiceImpl
 * @author abel
 * @date 2016年11月10日
 */
@Service
@Transactional
public class PayServiceImpl implements PayService {

	@Autowired
	private PayDao payDao;

	@Autowired
	private EventProducerDao eventProducerDao;

    @Override
    public Pay findByUsername(String username) {
        return payDao.findByUsername(username);
    }

    @Override
    public Pay findByUserId(String userId) {
        return payDao.findByUserId(userId);
    }

    @Override
    public Pay getPay(Long id) {
        return payDao.getOne(id);
    }

    public void updateAmountByUsername(BigDecimal amount, String mode, String username){
        if("IN".equals(mode)){
            amount =  new BigDecimal(Math.abs(amount.doubleValue()));
        }else if("OUT".equals(mode)){
            amount =  new BigDecimal(0D - Math.abs(amount.doubleValue()));
        }
        int i = payDao.updateAmountByUsername(amount, username);
        if(i != 1){
            throw new IllegalArgumentException("can't find username");
        }
    }

    public void updateAmountByUsername(BigDecimal amount, String mode, String username, Message msg){
		updateAmountByUsername(amount, mode, username);
        //记录日志
        commitMessage(msg);
	}

	@Override
	public void commitMessage(Message msg) {
		//更新消息日志表中记录
        int lines = eventProducerDao.updateStatusByTopicAdnMsgKey(MessageStatus.SUBMITTED, msg.getTopic(), msg.getKeys());
        if(lines < 1){
            throw new IllegalArgumentException("unknown topic["+msg.getTopic()+"] and keys:["+msg.getKeys()+"]");
        }
    }

	@Override
	public void rollBackMessage(Message msg) {
        //更新消息日志表中记录
        int lines = eventProducerDao.updateStatusByTopicAdnMsgKey(MessageStatus.ROLL_BACK, msg.getTopic(), msg.getKeys());
        if(lines < 1){
            throw  new IllegalArgumentException("unknown topic["+msg.getTopic()+"] and keys:["+msg.getKeys()+"]");
        }
	}

	public void updateDetail(Pay pay, String detail){
		Pay pay1 = payDao.getOne(pay.getId());
		pay.setDetail(detail);
		payDao.save(pay1);
	}
}
