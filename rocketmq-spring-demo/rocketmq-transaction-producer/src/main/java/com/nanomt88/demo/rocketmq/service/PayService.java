package com.nanomt88.demo.rocketmq.service;

import com.nanomt88.demo.rocketmq.entity.Pay;
import org.apache.rocketmq.common.message.Message;

import java.math.BigDecimal;

/**
 * The Interface PayService.
 */
public interface PayService {

	Pay findByUsername(String username);

	Pay findByUserId(String userId);

	Pay getPay(Long id);

	void updateAmountByUsername(BigDecimal amount, String mode, String username, Message msg);

	void updateAmountByUsername(BigDecimal amount, String mode, String username);

	void commitMessage(Message msg);

	void rollBackMessage(Message msg);

	public void updateDetail(Pay pay, String detail);
}
