package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.entity.User;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The Interface BalanceService.
 */
public interface BalanceService {
	

	void updateAmountByUsername(BigDecimal amount, String mode, String username);
	
	
}
