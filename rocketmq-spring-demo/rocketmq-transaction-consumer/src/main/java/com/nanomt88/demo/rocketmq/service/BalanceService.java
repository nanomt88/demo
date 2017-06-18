package com.nanomt88.demo.rocketmq.service;

import java.math.BigDecimal;

/**
 * The Interface BalanceService.
 */
public interface BalanceService {
	

	void updateAmountByUsername(BigDecimal amount, String mode, String username);
	
	
}
