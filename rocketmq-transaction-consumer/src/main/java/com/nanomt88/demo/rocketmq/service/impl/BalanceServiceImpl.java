package com.nanomt88.demo.rocketmq.service.impl;


import com.nanomt88.demo.rocketmq.dao.BalanceDao;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 
 * @ClassName UserServiceImpl
 * @author abel
 * @date 2016年11月10日
 */
@Service
public class BalanceServiceImpl implements BalanceService {

	@Autowired
	private BalanceDao balanceDao;


	public void updateAmountByUsername(BigDecimal amount, String mode, String username){
		if("IN".equals(mode)){
			amount =  new BigDecimal(0D - Math.abs(amount.doubleValue()));
		}else if("OUT".equals(mode)){
			amount =  new BigDecimal(Math.abs(amount.doubleValue()));
		}
		balanceDao.updateAmountByUsername(amount, username);
	}

}
