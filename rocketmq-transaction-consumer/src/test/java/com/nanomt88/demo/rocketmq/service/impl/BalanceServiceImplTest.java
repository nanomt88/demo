package com.nanomt88.demo.rocketmq.service.impl;

import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.dao.BalanceDao;
import com.nanomt88.demo.rocketmq.entity.Balance;
import com.nanomt88.demo.rocketmq.service.BalanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by ZBOOK-17 on 2017/6/14.
 */

@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类
//@SpringApplicationConfiguration(classes = Application.class)// 1.4.0 前版本
public class BalanceServiceImplTest {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceDao balanceDao;

    @Test
    public void updateAmountByUsername() throws Exception {

        String username = "张三";
        Balance balance = balanceDao.getOne(1L);
        BigDecimal amount = balance.getAmount();

        balanceService.updateAmountByUsername(new BigDecimal(100), "IN",username);

        Balance after = balanceDao.findOne(balance.getId());

        assertEquals(amount.doubleValue() + 100, after.getAmount().doubleValue(), 0);
    }

}