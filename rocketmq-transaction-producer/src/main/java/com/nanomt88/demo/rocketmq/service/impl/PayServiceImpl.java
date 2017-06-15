package com.nanomt88.demo.rocketmq.service.impl;


import com.nanomt88.demo.rocketmq.dao.PayDao;
import com.nanomt88.demo.rocketmq.entity.Pay;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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


	public void updateAmountByUsername(BigDecimal amount, String mode, String username){
		if("IN".equals(mode)){
			amount =  new BigDecimal(Math.abs(amount.doubleValue()));
		}else if("OUT".equals(mode)){
			amount =  new BigDecimal(0D - Math.abs(amount.doubleValue()));
		}
		payDao.updateAmountByUsername(amount, username);
	}

	public void updateDetail(Pay pay, String detail){
		Pay pay1 = payDao.getOne(pay.getId());
		pay.setDetail(detail);
		payDao.save(pay1);
	}
}
