package com.nanomt88.demo.rocketmq.service.impl;


import com.nanomt88.demo.rocketmq.dao.BalanceDao;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * 
 * @ClassName BalanceServiceImpl
 * @author abel
 * @date 2016年11月10日
 */
@Service
@Transactional
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceDao balanceDao;

    @Override
	public void updateAmountByUsername(BigDecimal amount, String mode, String username){
		if("IN".equals(mode)){
			amount =  new BigDecimal(Math.abs(amount.doubleValue()));
		}else if("OUT".equals(mode)){
			amount =  new BigDecimal(0D - Math.abs(amount.doubleValue()));
		}
		int i = balanceDao.updateAmountByUsername(amount, username);
		if(i != 1){
			throw new IllegalArgumentException("can't find username");
		}
	}
}
