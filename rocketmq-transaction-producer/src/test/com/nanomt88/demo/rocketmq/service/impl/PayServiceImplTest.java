package com.nanomt88.demo.rocketmq.service.impl;

import com.nanomt88.demo.boot.Main;
import com.nanomt88.demo.rocketmq.dao.PayDao;
import com.nanomt88.demo.rocketmq.entity.Pay;
import com.nanomt88.demo.rocketmq.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;


@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = Main.class)  // 指定spring-boot的启动类  //  1.4.0 以后版本
//@SpringApplicationConfiguration(classes = Application.class)// 1.4.0 前版本
public class PayServiceImplTest {

    @Autowired
    PayService payService;

    @Autowired
    PayDao payDao;

    @Test
    public void updateAmountByUsername() throws Exception {
        String username = "张三";

        Pay pay = payDao.getOne(1L);
        BigDecimal amount = pay.getAmount();

        payService.updateAmountByUsername(new BigDecimal(100), "IN", username);

        Pay pay2 = payDao.getOne(1L);

        assertEquals(amount.add(new BigDecimal(100)), pay2.getAmount());

    }

}