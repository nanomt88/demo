package com.nanomt88.demo.rocketmq.service;

import java.math.BigDecimal;

/**
 * The Interface PayService.
 */
public interface PayService {
	

	void updateAmountByUsername(BigDecimal amount, String mode, String username);
	
	
}
